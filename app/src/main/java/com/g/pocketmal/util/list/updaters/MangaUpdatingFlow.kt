package com.g.pocketmal.util.list.updaters

import android.app.Activity
import androidx.annotation.StyleRes
import com.g.pocketmal.domain.UpdateParameters
import com.g.pocketmal.domain.entity.ListRecord
import com.g.pocketmal.util.Action

class MangaUpdatingFlow(
    private val activity: Activity,
    @StyleRes private val themeResId: Int
) : RecordUpdatingFlow() {

    override fun updateTitle(actionType: Action, record: ListRecord, params: UpdateParameters) {

        /*val chapters = params.chapters
        val volumes = params.volumes
        val status = params.status

        if (chapters != null || volumes != null) {

            if (chapters != null && chapters == 1 && !record.myRe) {
                params.startDate = getTodayDate()
            }

            if (chapters != null && record.seriesEpisodes == chapters && record.seriesEpisodes != 0 || volumes != null && record.seriesSubEpisodes == volumes && record.seriesSubEpisodes != 0) {
                if (!record.myRe) {
                    showCompletedDialog(record, volumes != null)
                } else {
                    params.chapters = record.seriesEpisodes
                    params.reReading = false
                    params.reReadTimes = record.myReTimes + 1
                    executeUpdate(Action.ACTION_REWATCHED, record, params)
                }
            } else if (record.myStatus != Status.IN_PROGRESS && !record.myRe) {
                showReadingDialog(record, params)
            } else {
                params.chapters = chapters
                executeUpdate(
                    if (volumes != null) Action.ACTION_VOLUMES else Action.ACTION_CHAPTERS,
                    record,
                    params
                )
            }

        } else if (status != null) {

            if (status == Status.COMPLETED.status) {

                if (record.myScore == 0) {

                    ScoreDialog(activity, themeResId,
                        scoreListener = { score ->
                            params.chapters = record.seriesEpisodes
                            params.volumes = record.seriesSubEpisodes
                            params.score = score
                            params.startDate =
                                if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null
                            params.finishDate = getTodayDate()
                            executeUpdate(Action.ACTION_STATUS, record, params)
                        },
                        cancelListener = {
                            params.chapters = record.seriesEpisodes
                            params.volumes = record.seriesSubEpisodes
                            params.startDate =
                                if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null
                            params.finishDate = getTodayDate()
                            executeUpdate(Action.ACTION_STATUS, record, params)
                        }).show()
                } else {

                    params.chapters = record.seriesEpisodes
                    params.volumes = record.seriesSubEpisodes
                    params.startDate =
                        if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null
                    params.finishDate = getTodayDate()
                    executeUpdate(Action.ACTION_STATUS, record, params)
                }
            } else {
                params.status = status
                executeUpdate(Action.ACTION_STATUS, record, params)
            }
        } else {
            executeUpdate(actionType, record, params)
        }*/
    }

    private fun showCompletedDialog(record: ListRecord, volumesChanges: Boolean) {

        /*YesNoDialog(activity, themeResId, activity.getString(R.string.setMangaAsCompleted),
            yesClick = {
                if (record.myScore == 0) {

                    ScoreDialog(activity, themeResId,
                        scoreListener = { score ->
                            val params = UpdateParams(
                                chapters = record.seriesEpisodes,
                                volumes = record.seriesSubEpisodes,
                                status = Status.COMPLETED.status,
                                score = score,
                                startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                finishDate = getTodayDate()
                            )
                            executeUpdate(Action.ACTION_STATUS, record, params)
                        },
                        cancelListener = {
                            val params = UpdateParams(
                                chapters = record.seriesEpisodes,
                                volumes = record.seriesSubEpisodes,
                                status = Status.COMPLETED.status,
                                startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                finishDate = getTodayDate()
                            )
                            executeUpdate(Action.ACTION_STATUS, record, params)
                        }).show()
                } else {
                    val params = UpdateParams(
                        chapters = record.seriesEpisodes,
                        volumes = record.seriesSubEpisodes,
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
                                chapters = record.seriesEpisodes,
                                volumes = record.seriesSubEpisodes,
                                score = score,
                                startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                finishDate = getTodayDate()
                            )
                            executeUpdate(Action.ACTION_STATUS, record, params)
                        },
                        cancelListener = {
                            val params = UpdateParams(
                                chapters = record.seriesEpisodes,
                                volumes = record.seriesSubEpisodes,
                                startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                                finishDate = getTodayDate()
                            )
                            executeUpdate(Action.ACTION_STATUS, record, params)
                        }).show()
                } else {
                    val params = UpdateParams(
                        chapters = record.seriesEpisodes,
                        volumes = record.seriesSubEpisodes,
                        startDate = if (record.seriesEpisodes == 1 && record.myStartDate == null) getTodayDate() else null,
                        finishDate = getTodayDate()
                    )
                    executeUpdate(
                        if (volumesChanges) Action.ACTION_VOLUMES else Action.ACTION_CHAPTERS,
                        record,
                        params
                    )
                }
            }
        ).show()*/
    }

    private fun showReadingDialog(record: ListRecord, params: UpdateParameters) {

        /*YesNoDialog(activity, themeResId, activity.getString(R.string.setAsReading),
            yesClick = {
                params.status = Status.IN_PROGRESS.status
                executeUpdate(Action.ACTION_STATUS, record, params)
            },
            noClick = {
                executeUpdate(
                    if (params.chapters == null) Action.ACTION_VOLUMES else Action.ACTION_CHAPTERS,
                    record,
                    params
                )
            }
        ).show()*/
    }
}
