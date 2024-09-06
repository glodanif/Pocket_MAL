package com.g.pocketmal.ui.legacy.presenter

import android.text.TextUtils
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.data.api.SessionExpiredException
import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.exception.UnsynchronizedListException
import com.g.pocketmal.domain.interactor.LogoutInteractor
import com.g.pocketmal.domain.interactor.UpdateTitleInteractor
import com.g.pocketmal.util.Action
import com.g.pocketmal.util.EpisodeType
import com.g.pocketmal.util.list.ListsManager
import com.g.pocketmal.util.list.updaters.AnimeUpdatingFlow
import com.g.pocketmal.util.list.updaters.MangaUpdatingFlow

abstract class BaseUpdatingPresenter(
    private val sessionView: com.g.pocketmal.ui.legacy.view.BaseSessionView,
    private val sessionRoute: com.g.pocketmal.ui.legacy.route.BaseSessionRoute,
    private val listsManager: ListsManager,
    private val logoutInteractor: LogoutInteractor,
    private val updateTitleInteractor: UpdateTitleInteractor
) : BasePresenter(sessionView, sessionRoute, logoutInteractor) {

    private lateinit var animeUpdater: AnimeUpdatingFlow
    private lateinit var mangaUpdater: MangaUpdatingFlow

    fun attachUpdaters(animeUpdater: AnimeUpdatingFlow, mangaUpdater: MangaUpdatingFlow) {
        this.animeUpdater = animeUpdater
        this.animeUpdater.listener = { action, model, params ->
            update(action, model, params)
        }
        this.mangaUpdater = mangaUpdater
        this.mangaUpdater.listener = { action, model, params ->
            update(action, model, params)
        }
    }

    protected abstract fun onUpdate()

    protected abstract fun onUpdated(action: Action, updated: DbListRecord)

    protected abstract fun onUpdateFailed(throwable: Throwable)

    protected abstract fun onAlreadyWatchedAllEpisodes(episodeType: EpisodeType)

    protected abstract fun onRewatched(times: Int, titleType: TitleType)

    private fun update(action: Action, record: DbListRecord, params: UpdateParams) {

        onUpdate()

        updateTitleInteractor.execute(UpdateTitleInteractor.Params(record.seriesId, record.titleType, params),
                onResult = { dbRecord ->
                    listsManager.remove(record.seriesId, record.titleType)
                    listsManager.add(dbRecord)
                    onUpdated(action, dbRecord)

                    if (action == Action.ACTION_REWATCHED) {
                        onRewatched(dbRecord.myReTimes, dbRecord.titleType)
                    }
                },
                onError = { throwable ->
                    if (throwable is SessionExpiredException) {
                        forceLogout()
                    } else {
                        onUpdateFailed(throwable)
                    }
                }
        )
    }

    fun updateTitle(action: Action, seriesId: Int, titleType: TitleType, params: UpdateParams, directUpdate: Boolean = false) {

        val dbRecord = listsManager.getTitleFromGeneralList(seriesId, titleType)
        if (dbRecord == null) {
            onUpdateFailed(UnsynchronizedListException("Unable to find this title in ListsManager"))
            return
        }

        if (directUpdate) {
            update(action, dbRecord, params)
        } else {
            if (dbRecord.titleType == TitleType.ANIME) {
                animeUpdater.updateTitle(action, dbRecord, params)
            } else {
                mangaUpdater.updateTitle(action, dbRecord, params)
            }
        }
    }

    fun incrementEpisode(seriesId: Int, titleType: TitleType) {

        val dbRecord = listsManager.getTitleFromGeneralList(seriesId, titleType)
        if (dbRecord == null) {
            onUpdateFailed(UnsynchronizedListException("Unable to find this title in ListsManager"))
            return
        }

        val isAnime = dbRecord.titleType == TitleType.ANIME

        if (dbRecord.seriesEpisodes == dbRecord.myEpisodes && dbRecord.myEpisodes != 0) {
            onAlreadyWatchedAllEpisodes(if (isAnime) EpisodeType.EPISODE else EpisodeType.CHAPTER)
        } else {

            val updateParams = if (isAnime)
                UpdateParams(episodes = dbRecord.myEpisodes + 1) else UpdateParams(chapters = dbRecord.myEpisodes + 1)
            if (isAnime) {
                updateTitle(Action.ACTION_EPISODES, seriesId, titleType, updateParams)
            } else {
                updateTitle(Action.ACTION_CHAPTERS, seriesId, titleType, updateParams)
            }
        }
    }

    fun incrementSubEpisode(seriesId: Int, titleType: TitleType) {

        val dbRecord = listsManager.getTitleFromGeneralList(seriesId, titleType)
        if (dbRecord == null) {
            onUpdateFailed(UnsynchronizedListException("Unable to find this title in ListsManager"))
            return
        }

        if (dbRecord.seriesSubEpisodes == dbRecord.mySubEpisodes && dbRecord.mySubEpisodes != 0) {
            onAlreadyWatchedAllEpisodes(EpisodeType.VOLUME)
        } else {
            val updateParams = UpdateParams(volumes = dbRecord.mySubEpisodes + 1)
            updateTitle(Action.ACTION_VOLUMES, seriesId, titleType, updateParams)
        }
    }

    fun setNewStatus(seriesId: Int, titleType: TitleType, status: Status) {

        val dbRecord = listsManager.getTitleFromGeneralList(seriesId, titleType)
        if (dbRecord == null) {
            onUpdateFailed(UnsynchronizedListException("Unable to find this title in ListsManager"))
            return
        }

        if (dbRecord.myStatus != status) {
            val params = UpdateParams(status = status.status)
            updateTitle(Action.ACTION_STATUS, seriesId, titleType, params)
        }
    }

    fun setNewScore(seriesId: Int, titleType: TitleType, score: Int) {

        val dbRecord = listsManager.getTitleFromGeneralList(seriesId, titleType)
        if (dbRecord == null) {
            onUpdateFailed(UnsynchronizedListException("Unable to find this title in ListsManager"))
            return
        }

        if (dbRecord.myScore != score) {
            val params = UpdateParams(score = score)
            updateTitle(Action.ACTION_SCORE, seriesId, titleType, params)
        }
    }

    fun updateEpisodes(seriesId: Int, titleType: TitleType, episodes: Int?, subEpisodes: Int?) {

        val dbRecord = listsManager.getTitleFromGeneralList(seriesId, titleType)
        if (dbRecord == null) {
            onUpdateFailed(UnsynchronizedListException("Unable to find this title in ListsManager"))
            return
        }

        if (dbRecord.titleType == TitleType.ANIME) {
            if (dbRecord.myEpisodes != episodes) {
                val params = UpdateParams(episodes = episodes)
                updateTitle(Action.ACTION_EPISODES, seriesId, titleType, params)
            }
        } else {
            if (dbRecord.myEpisodes != episodes || dbRecord.mySubEpisodes != subEpisodes) {
                val params = UpdateParams(chapters = episodes, volumes = subEpisodes)
                updateTitle(Action.ACTION_CHAPTERS, seriesId, titleType, params)
            }
        }
    }

    fun saveNewTags(seriesId: Int, titleType: TitleType, tags: String) {

        val dbRecord = listsManager.getTitleFromGeneralList(seriesId, titleType)
        if (dbRecord == null) {
            onUpdateFailed(UnsynchronizedListException("Unable to find this title in ListsManager"))
            return
        }

        val currentTags = TextUtils.join(", ", dbRecord.myTags)
        if (currentTags != tags) {
            val array = tags.split(",").map { it.trim() }
            val list = UpdateParams.StringableList()
            list.addAll(array)
            val params = UpdateParams(tags = list)
            updateTitle(Action.ACTION_TAGS, seriesId, titleType, params)
        }
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun stop() {
        updateTitleInteractor.cancel()
    }
}
