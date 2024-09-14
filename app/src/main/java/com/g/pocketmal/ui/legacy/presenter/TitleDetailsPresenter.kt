package com.g.pocketmal.ui.legacy.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.R
import com.g.pocketmal.data.api.SessionExpiredException
import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.data.platform.ClipboardManager
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.di.DataModule
import com.g.pocketmal.domain.exception.NotApprovedTitleException
import com.g.pocketmal.domain.exception.UnsynchronizedListException
import com.g.pocketmal.domain.interactor.AddTitleToListInteractor
import com.g.pocketmal.domain.interactor.GetRecordFromDbInteractor
import com.g.pocketmal.domain.interactor.GetTitleDetailsInteractor
import com.g.pocketmal.domain.interactor.LogoutInteractor
import com.g.pocketmal.domain.interactor.RemoveTitleFromListInteractor
import com.g.pocketmal.domain.interactor.UpdateTitleInteractor
import com.g.pocketmal.ordinal
import com.g.pocketmal.ui.legacy.viewentity.converter.ListItemConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.TitleDetailsConverter
import com.g.pocketmal.util.Action
import com.g.pocketmal.util.EpisodeType
import com.g.pocketmal.util.list.ListsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TitleDetailsPresenter(
    private var recordId: Int,
    private var titleType: TitleType,
    private val view: com.g.pocketmal.ui.legacy.view.TitleDetailsView,
    private val route: com.g.pocketmal.ui.legacy.route.TitleDetailsRoute,
    private val listsManager: ListsManager,
    private val settings: UserSettings,
    private val recordConverter: ListItemConverter,
    private val detailsConverter: TitleDetailsConverter,
    private val clipboardManager: ClipboardManager,
    private val getTitleDetailsInteractor: GetTitleDetailsInteractor,
    private val addTitleToListInteractor: AddTitleToListInteractor,
    private val removeTitleFromListInteractor: RemoveTitleFromListInteractor,
    private val getRecordFromDbInteractor: GetRecordFromDbInteractor,
    private val updateTitleInteractor: UpdateTitleInteractor,
    private val logoutInteractor: LogoutInteractor,
    private val userPreferences: DataModule.DataStoreEntryPoint,
) : BaseUpdatingPresenter(
    view,
    route,
    listsManager,
    logoutInteractor,
    updateTitleInteractor
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    //FIXME get rid of state!
    private var currentRecord: com.g.pocketmal.ui.legacy.viewentity.RecordViewModel? = null
    var notLoadedAnythingLastTime = false

    private var externalLinksPattern = ""

    init {
        watchUserPreferences()
    }

    private fun watchUserPreferences() {
        coroutineScope.launch {
            userPreferences.getUserPreferencesDataStore().data.collect { preferences ->
                externalLinksPattern = preferences.externalLinkPattern
                view.externalLinksPatternChanged(externalLinksPattern)
            }
        }
    }

    fun loadRecord(networkUpdate: Boolean) {

        getRecordFromDbInteractor.execute(GetRecordFromDbInteractor.Params(recordId, titleType),
            onResult = { record ->
                currentRecord = recordConverter.transform(record)
                currentRecord?.let { view.displayRecord(it) }
                view.resetToolbarMenu()
            },
            onError = {
                view.showNotInListLayout()
            }
        )

        loadDetails(networkUpdate)
    }

    fun loadDetails(networkUpdate: Boolean, retry: Boolean = false) {

        var wasPreviouslyLoaded = false
        view.showDetailsLoading()

        if (notLoadedAnythingLastTime && !retry) {
            view.showUnableToLoadAnything()
        } else {
            notLoadedAnythingLastTime = false
            view.hideUnableToLoadAnything()
        }

        val useEnglishTitles = settings.getShowEnglishTitles()

        getTitleDetailsInteractor.execute(GetTitleDetailsInteractor.Params(recordId, titleType),
            skipNetwork = !networkUpdate,
            onCacheResult = { result ->

                view.hideUnableToLoadAnything()
                val viewModel = detailsConverter.transform(result.details)
                view.displayDetails(viewModel, useEnglishTitles)

                if (currentRecord == null) {
                    val currentRecord = recordConverter.createFrame(recordId, titleType, viewModel)
                    view.displayRecord(currentRecord, viewModel, useEnglishTitles)
                }
                wasPreviouslyLoaded = true
                view.hideDetailsLoading()
            },
            onNetworkResult = { result ->

                view.hideUnableToLoadAnything()
                val viewModel = detailsConverter.transform(result.details)
                view.displayDetails(viewModel, useEnglishTitles)

                if (result.record != null) {

                    val record = result.record
                    val viewRecord = recordConverter.transform(record)
                    currentRecord = viewRecord
                    view.displayRecord(viewRecord, viewModel, useEnglishTitles)
                    listsManager.remove(recordId, titleType)
                    listsManager.add(record)
                } else {
                    val viewRecord = recordConverter.createFrame(recordId, titleType, viewModel)
                    currentRecord = viewRecord
                    view.displayRecord(viewRecord, viewModel, useEnglishTitles)
                }

            },
            onNetworkError = { throwable ->

                if (throwable is SessionExpiredException) {
                    view.notifyUserAboutForceLogout()
                    route.redirectToLoginScreen()
                } else {
                    if (wasPreviouslyLoaded) {
                        view.showFailedToUpdateTitleThroughNetwork()
                    } else {
                        notLoadedAnythingLastTime = true
                        view.showUnableToLoadAnything()
                    }
                }
            },
            onComplete = {
                view.hideSyncIndicator()
                view.hideDetailsLoading()
            }
        )
    }

    fun updateTitle(action: Action, params: UpdateParams, directUpdate: Boolean = false) {
        updateTitle(action, recordId, titleType, params, directUpdate)
    }

    fun incrementEpisode() {
        incrementEpisode(recordId, titleType)
    }

    fun incrementSubEpisode() {
        incrementSubEpisode(recordId, titleType)
    }

    fun setNewStatus(status: Status) {
        setNewStatus(recordId, titleType, status)
    }

    fun setNewScore(score: Int) {
        setNewScore(recordId, titleType, score)
    }

    fun updateEpisodes(episodes: Int?, subEpisodes: Int?) {
        updateEpisodes(recordId, titleType, episodes, subEpisodes)
    }

    fun saveNewTags(tags: String) {
        saveNewTags(recordId, titleType, tags)
    }

    fun editRewatching() {
        route.openDetailsEditorForResult(recordId, titleType)
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
        currentRecord = newRecord
        view.displayRecord(newRecord)
        view.showActions(newRecord, action)
        view.hideLoadingDialog()
    }

    override fun onRewatched(times: Int, titleType: TitleType) {
        val text = if (titleType == TitleType.ANIME)
            R.string.rewatching__rewatched_times_message else R.string.rewatching__rereading_times_message
        view.showRewatchedPopup(times.ordinal(), text)
    }

    override fun onUpdateFailed(throwable: Throwable) {
        if (throwable is UnsynchronizedListException) {
            view.showUnsynchronizedFailure()
        } else {
            view.showUpdatingFailure()
            view.hideLoadingDialog()
        }
    }

    fun resetTags() {
        currentRecord?.let { record ->
            view.setTags(record.myTags)
        }
    }

    fun addTitleToMyList() {

        view.showLoadingDialog(R.string.adding)

        addTitleToListInteractor.execute(AddTitleToListInteractor.Params(recordId, titleType),
            onResult = { dbRecord ->

                listsManager.add(dbRecord)
                val justAddedViewModel = recordConverter.transform(dbRecord)
                currentRecord = justAddedViewModel

                view.displayNewlyAddedTitle(justAddedViewModel)
                view.showActions(justAddedViewModel, Action.ACTION_STATUS)
                view.resetToolbarMenu()
            },
            onError = { throwable ->
                if (throwable is NotApprovedTitleException) {
                    view.showAddingFailureBecauseNotApproved()
                } else {
                    view.showAddingFailure()
                }
                view.showNotInListLayout()
            },
            onComplete = {
                view.hideLoadingDialog()
            }
        )
    }

    fun removeFromMyList() {

        view.showLoadingDialog(R.string.removing)

        removeTitleFromListInteractor.execute(RemoveTitleFromListInteractor.Params(
            recordId,
            titleType
        ),
            onResult = {
                listsManager.remove(recordId, titleType)
                currentRecord = recordConverter.createFrame(recordId, titleType)
                view.showNotInListLayout()
                view.resetToolbarMenu()
            },
            onError = {
                view.showRemovingFailure()
            },
            onComplete = {
                view.hideLoadingDialog()
            }
        )
    }

    //FIXME don't call presenter from view?
    fun isInMyList() = currentRecord != null && currentRecord?.myStatus != Status.NOT_IN_LIST

    fun onExternalLinkClick() {
        currentRecord?.let {
            if (externalLinksPattern.isEmpty()) {
                route.openExternalLinkSetup()
            } else {
                val externalLink = "https://" + externalLinksPattern
                    .replace("{type}", if (titleType == TitleType.ANIME) "anime" else "manga")
                    .replace("{id}", recordId.toString())
                route.openExternalLink(externalLink)
            }
        }
    }

    fun onEditDetailsClick() {
        currentRecord?.let {
            route.openDetailsEditorForResult(recordId, titleType)
        }
    }

    fun copyTitleToClipboard() {

        if (currentRecord == null) {
            view.unableToCopy()
            return
        }

        currentRecord?.let {
            clipboardManager.copyToClipboard(it.seriesTitle)
            view.showCopied()
        }
    }

    fun shareTitle() {
        currentRecord?.let {
            route.shareTitle(it.malLink)
        }
    }

    fun discussTitle() {
        currentRecord?.let {
            route.openDiscussion(it.discussionLink)
        }
    }

    fun showPoster() {
        currentRecord?.let {
            route.openPoster(it.seriesImage)
        }
    }

    fun onRecommendationsClick() {
        route.openRecommendations(recordId, titleType)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun stop() {
        super.stop()
        getTitleDetailsInteractor.cancel()
        removeTitleFromListInteractor.cancel()
        addTitleToListInteractor.cancel()
        getRecordFromDbInteractor.cancel()
    }
}
