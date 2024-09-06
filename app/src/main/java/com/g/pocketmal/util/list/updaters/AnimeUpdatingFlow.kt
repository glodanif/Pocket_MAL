package com.g.pocketmal.util.list.updaters

import androidx.annotation.StyleRes
import com.g.pocketmal.R
import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.legacy.dialog.ScoreDialog
import com.g.pocketmal.ui.legacy.dialog.YesNoDialog
import com.g.pocketmal.util.Action

class AnimeUpdatingFlow(private val activity: SkeletonActivity, @StyleRes private val themeResId: Int) : RecordUpdatingFlow() {

    override fun updateTitle(actionType: Action, record: DbListRecord, params: UpdateParams) {

        val episodes = params.episodes
        val status = params.status

        if (episodes != null) {

            if (episodes == 1 && !record.myRe) {
                params.startDate = getTodayDate()
            }

            if (episodes == record.seriesEpisodes && record.seriesEpisodes != 0) {
                if (!record.myRe) {
                    showCompletedDialog(record)
                } else {
                    params.episodes = record.seriesEpisodes
                    params.reWatching = false
                    params.reWatchedTimes = record.myReTimes + 1

                    executeUpdate(Action.ACTION_REWATCHED, record, params)
                }
            } else if (record.myStatus != Status.IN_PROGRESS && !record.myRe) {
                showWatchingDialog(record, params)
            } else {
                params.episodes = episodes
                executeUpdate(Action.ACTION_EPISODES, record, params)
            }

        } else if (status != null) {

            if (status == Status.COMPLETED.status) {

                if (record.myScore == 0) {

                    ScoreDialog(activity, themeResId,
                            scoreListener = { score ->
                                params.episodes = record.seriesEpisodes
                                params.score = score
                                params.startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null
                                params.finishDate = getTodayDate()
                                executeUpdate(Action.ACTION_STATUS, record, params)
                            },
                            cancelListener = {
                                params.episodes = record.seriesEpisodes
                                params.startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null
                                params.finishDate = getTodayDate()
                                executeUpdate(Action.ACTION_STATUS, record, params)
                            }).show()

                } else {

                    params.episodes = record.seriesEpisodes
                    params.startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null
                    params.finishDate = getTodayDate()
                    executeUpdate(Action.ACTION_STATUS, record, params)
                }
            } else {
                params.status = status
                executeUpdate(Action.ACTION_STATUS, record, params)
            }
        } else {
            executeUpdate(actionType, record, params)
        }
    }

    private fun showCompletedDialog(record: DbListRecord) {

        YesNoDialog(activity, themeResId, activity.getString(R.string.setAnimeAsCompleted),
                yesClick = {
                    if (record.myScore == 0) {

                        ScoreDialog(activity, themeResId,
                                scoreListener = { score ->
                                    val params = UpdateParams(
                                            episodes = record.seriesEpisodes,
                                            status = Status.COMPLETED.status,
                                            score = score,
                                            startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                            finishDate = getTodayDate()
                                    )
                                    executeUpdate(Action.ACTION_STATUS, record, params)
                                },
                                cancelListener = {
                                    val params = UpdateParams(
                                            episodes = record.seriesEpisodes,
                                            status = Status.COMPLETED.status,
                                            startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                            finishDate = getTodayDate()
                                    )
                                    executeUpdate(Action.ACTION_STATUS, record, params)
                                }).show()
                    } else {

                        val params = UpdateParams(
                                episodes = record.seriesEpisodes,
                                status = Status.COMPLETED.status,
                                startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                finishDate = getTodayDate()
                        )
                        executeUpdate(Action.ACTION_STATUS, record, params)
                    }
                },
                noClick = {
                    if (record.myScore == 0) {

                        ScoreDialog(activity, themeResId,
                                scoreListener = { score ->
                                    val params = UpdateParams(
                                            episodes = record.seriesEpisodes,
                                            score = score,
                                            startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                            finishDate = getTodayDate()
                                    )
                                    executeUpdate(Action.ACTION_STATUS, record, params)
                                },
                                cancelListener = {
                                    val params = UpdateParams(
                                            episodes = record.seriesEpisodes,
                                            startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                            finishDate = getTodayDate()
                                    )
                                    executeUpdate(Action.ACTION_STATUS, record, params)
                                }).show()
                    } else {

                        val params = UpdateParams(
                                episodes = record.seriesEpisodes,
                                startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                finishDate = getTodayDate()
                        )
                        executeUpdate(Action.ACTION_EPISODES, record, params)
                    }
                }
        ).show()
    }

    private fun showWatchingDialog(record: DbListRecord, params: UpdateParams) {

        YesNoDialog(activity, themeResId, activity.getString(R.string.setAsWatching),
                yesClick = {
                    params.status = Status.IN_PROGRESS.status
                    executeUpdate(Action.ACTION_STATUS, record, params)
                },
                noClick = {
                    executeUpdate(Action.ACTION_EPISODES, record, params)
                }
        ).show()
    }
}
