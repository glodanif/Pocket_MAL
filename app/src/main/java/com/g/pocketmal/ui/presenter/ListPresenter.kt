package com.g.pocketmal.ui.presenter

import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.R
import com.g.pocketmal.data.api.SessionExpiredException
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.keyvalue.SessionManager
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.comparator.RecordComparator
import com.g.pocketmal.domain.comparator.SortingType
import com.g.pocketmal.domain.exception.ListAccessException
import com.g.pocketmal.domain.exception.MalDownException
import com.g.pocketmal.domain.interactor.GetListsFromDbInteractor
import com.g.pocketmal.domain.interactor.LoadListFromNetworkInteractor
import com.g.pocketmal.domain.interactor.LogoutInteractor
import com.g.pocketmal.domain.interactor.UpdateTitleInteractor
import com.g.pocketmal.getTimePeriod
import com.g.pocketmal.ordinal
import com.g.pocketmal.ui.route.ListRoute
import com.g.pocketmal.ui.view.ListView
import com.g.pocketmal.ui.viewmodel.converter.ListItemConverter
import com.g.pocketmal.ui.viewmodel.converter.ListRecordConverter
import com.g.pocketmal.util.Action
import com.g.pocketmal.util.EpisodeType
import com.g.pocketmal.util.list.DataInterpreter
import com.g.pocketmal.util.list.ListsManager
import java.net.SocketTimeoutException
import java.util.*
import kotlin.collections.ArrayList

class ListPresenter(
        private val view: ListView,
        private val route: ListRoute,
        private val listsManager: ListsManager,
        private val localStorage: LocalStorage,
        private val settings: MainSettings,
        private val session: SessionManager,
        private val recordConverter: ListItemConverter,
        private val converter: ListRecordConverter,
        private val logoutInteractor: LogoutInteractor,
        private val loadListFromNetworkInteractor: LoadListFromNetworkInteractor,
        private val getListsFromDbInteractor: GetListsFromDbInteractor,
        private val updateTitleInteractor: UpdateTitleInteractor
) : BaseUpdatingPresenter(
        view,
        route,
        listsManager,
        logoutInteractor,
        updateTitleInteractor
) {

    private var comparator = RecordComparator()

    var status = Status.IN_PROGRESS
        private set
    var type = settings.defaultListType()
        private set
    var filter: String? = null
        private set

    private var shouldSwitchLists = false
    private var isPreInitialized = false
    private var isLoading = false

    fun restoreState(state: ListView.State) {
        this.status = state.status
        this.type = state.titleType
        this.filter = state.filter
    }

    fun attach() {

        this.status = Status.IN_PROGRESS
        this.type = settings.defaultListType()

        if (settings.isSortingStateSaving()) {
            comparator = RecordComparator(localStorage.getSortingType(), localStorage.getSortingReverse())
        }
    }

    fun deepLink(link: String?) = link?.let { url ->

        try {
            val linkParts = url.split("/")
            val id = Integer.valueOf(linkParts[4])
            val type = if ("anime" == linkParts[3]) TitleType.ANIME else TitleType.MANGA
            if (!session.isUserLoggedIn) {
                route.closeApp()
            }
            route.redirectToDetailsScreen(id, type)
        } catch (e: Exception) {
            view.showLinParsingError()
        }
    }

    fun initLists() {

        getListsFromDbInteractor.execute(Unit,
                onResult = { lists ->
                    if (lists.animeList != null) {
                        listsManager.initAnimeLists(lists.animeList)
                    }
                    if (lists.mangaList != null) {
                        listsManager.initMangaLists(lists.mangaList)
                    }
                },
                onComplete = {
                    isPreInitialized = true
                    setList()
                }
        )
    }

    fun localStorageRouter() {

        isPreInitialized = listsManager.isListInitialized(type)

        if (isPreInitialized) {
            setList()
        }

        if (!isPreInitialized || settings.isAutoSync()) {
            if (!listsManager.isActualList(type)) {
                loadListFromNetwork()
            }
        } else {
            val date = Date(localStorage.getLastSynchronizing(type))
            val headerLabel = date.getTimePeriod()
            view.showLastSyncHeader(headerLabel)
        }
    }

    fun loadListFromNetwork() {

        if (isLoading) {
            return
        }
        isLoading = true
        view.showSync(!isPreInitialized)

        val lockType = type
        loadListFromNetworkInteractor.execute(lockType,
                onResult = { list ->
                    listsManager.setListIsActual(lockType)
                    processList(list, lockType)
                    isPreInitialized = true
                },
                onError = { throwable ->
                    when (throwable) {
                        is SessionExpiredException -> {
                            forceLogout()
                        }
                        is ListAccessException -> {
                            view.showSyncFailedBecauseListIsPrivate(throwable.errorMessage)
                            view.hideLoadingDialog()
                        }
                        is MalDownException -> {
                            showSyncFailed(R.string.syncFailedMalDown)
                        }
                        is SocketTimeoutException -> {
                            showSyncFailed(R.string.syncFailedTimeout)
                        }
                        else -> {
                            showSyncFailed(R.string.syncFailed)
                        }
                    }
                },
                onComplete = {
                    isLoading = false
                }
        )
    }

    fun incrementEpisode(id: Int, titleType: TitleType, episodeType: EpisodeType) {
        if (episodeType != EpisodeType.VOLUME) {
            incrementEpisode(id, titleType)
        } else {
            incrementSubEpisode(id, titleType)
        }
    }

    private fun showSyncFailed(@StringRes message: Int) {
        val date = Date(localStorage.getLastSynchronizing(type))
        val lastSync = date.getTimePeriod()
        view.showSyncFailed(lastSync, message)
    }

    fun selectSorting() {
        route.displaySortingDialog(comparator.sortingType, comparator.reverse)
    }

    fun onNewSorting(type: SortingType, reverse: Boolean) {

        //FIXME: Move to interactor
        localStorage.storeSortingType(type)
        localStorage.storeSortingReverse(reverse)

        comparator = RecordComparator(type, reverse)
        onNewStatus(status)
    }

    fun onFilter(query: String?) {
        filter = query
    }

    private fun setList() {
        val counts = if (type == TitleType.ANIME)
            listsManager.animeCounts else listsManager.mangaCounts
        view.displayCounts(counts)
        view.hideSyncIndicator()
        onNewStatus(status)
    }

    private fun processList(list: List<DbListRecord>?, syncType: TitleType) {

        view.hideEmptyList()

        val viewModels = list ?: ArrayList()
        if (syncType == TitleType.ANIME) {
            listsManager.initAnimeLists(viewModels)
        } else {
            listsManager.initMangaLists(viewModels)
        }

        if (syncType == type) {
            setList()
        }
    }

    fun reloadList() {
        setList()
    }

    override fun onAlreadyWatchedAllEpisodes(episodeType: EpisodeType) {
        val label = when (episodeType) {
            EpisodeType.EPISODE -> R.string.watchedAllEpisodes
            EpisodeType.CHAPTER -> R.string.readLastChapter
            EpisodeType.VOLUME -> R.string.readLastVolume
        }
        view.showAllEpisodesAlreadyCompleted(label)
    }

    override fun onUpdate() {
        view.showLoadingDialog(R.string.updating)
    }

    override fun onUpdated(action: Action, updated: DbListRecord) {
        val newRecord = recordConverter.transform(updated)
        reloadList()
        view.showActions(newRecord, action)
        view.hideLoadingDialog()
    }

    override fun onRewatched(times: Int, titleType: TitleType) {
        val text = if (titleType == TitleType.ANIME)
            R.string.rewatching__rewatched_times_message else R.string.rewatching__rereading_times_message
        view.showRewatchedPopup(times.ordinal(), text)
    }

    override fun onUpdateFailed(throwable: Throwable) {
        view.showUpdatingFailure()
        view.hideLoadingDialog()
    }

    fun onNewStatus(status: Status) {
        this.status = status
        val list = listsManager.getListByStatus(status, type)
        val statusLabel = DataInterpreter.getStatusById(status, type)
        val typeLabel = DataInterpreter.getTypeLabel(type)
        if (list.isEmpty()) {
            view.displayEmptyList(statusLabel)
        } else {
            val useEnglishTitles = settings.showEnglishTitles()
            val viewModels = converter.transform(type, list, useEnglishTitles)
            Collections.sort(viewModels, comparator)
            view.displayList(viewModels, filter, status != Status.COMPLETED, settings.isSimpleViewMode(), settings.showTagsInList())
        }

        view.setupActionBar(typeLabel, statusLabel)
        view.setupDrawer(type)
    }

    fun swapLists(newType: TitleType) {

        if (newType == type) {
            return
        }

        type = if (type == TitleType.ANIME) TitleType.MANGA else TitleType.ANIME
        shouldSwitchLists = !shouldSwitchLists
        status = Status.IN_PROGRESS

        view.displayCounts(listsManager.getCountsByType(type))

        settings.setOpenedList(type)

        localStorageRouter()
    }

    fun redirectBack() {

        if (type == TitleType.ANIME && status != Status.IN_PROGRESS) {
            onNewStatus(Status.IN_PROGRESS)
        } else if (type == TitleType.MANGA && status != Status.IN_PROGRESS) {
            onNewStatus(Status.IN_PROGRESS)
        } else if (shouldSwitchLists) {
            swapLists(if (type == TitleType.ANIME) TitleType.MANGA else TitleType.ANIME)
        } else {
            route.closeApp()
        }
    }

    fun goToDetails(id: Int) {
        route.redirectToDetailsScreen(id, type)
    }

    fun goToSearch() {
        route.redirectToSearchScreen(type)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clearListFromMemory() {
        listsManager.clearInstance()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun stop() {
        super.stop()
        logoutInteractor.cancel()
        loadListFromNetworkInteractor.cancel()
        getListsFromDbInteractor.cancel()
    }
}

