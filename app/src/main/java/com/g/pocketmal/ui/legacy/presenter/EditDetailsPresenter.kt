package com.g.pocketmal.ui.legacy.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.R
import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.interactor.GetRecordFromDbInteractor
import com.g.pocketmal.util.DateType
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.ui.legacy.viewmodel.converter.ListItemConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EditDetailsPresenter(
    private val recordId: Int,
    private val titleType: TitleType,
    private val view: com.g.pocketmal.ui.legacy.view.EditDetailsView,
    private val route: com.g.pocketmal.ui.legacy.route.EditDetailsRoute,
    private val converter: ListItemConverter,
    private val getRecordFromDbInteractor: GetRecordFromDbInteractor
) : LifecycleObserver {

    private val viewFormatter: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    private val dateFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    private lateinit var currentRecord: DbListRecord

    var startDate: String? = null
    var finishDate: String? = null

    private var previousState: com.g.pocketmal.ui.legacy.view.EditDetailsView.State? = null

    fun restoreState(state: com.g.pocketmal.ui.legacy.view.EditDetailsView.State) {
        this.previousState = state
        this.startDate = state.startDate
        this.finishDate = state.finishDate
    }

    fun setupLayout() {
        val progressLabel = if (titleType == TitleType.ANIME)
            R.string.watchingPeriod else R.string.readingPeriod
        val reTimesLabel = if (titleType == TitleType.ANIME)
            R.string.totalTimesRewatched else R.string.totalTimesReread
        view.setupHeaders(progressLabel, reTimesLabel)
    }

    fun loadRecord() {

        getRecordFromDbInteractor.execute(GetRecordFromDbInteractor.Params(recordId, titleType),

                onResult = { record ->
                    currentRecord = record
                    val viewModel = converter.transform(record)
                    startDate = record.myStartDate
                    finishDate = record.myFinishDate
                    view.showStartDate(viewModel.myStartDate)
                    view.showFinishDate(viewModel.myFinishDate)
                    if (record.myStatus == Status.COMPLETED || record.myRe) {
                        view.setupReLayout(viewModel)
                        view.setupReChangeableInfo(viewModel.myRe, viewModel.myReTimes.toString(),
                                viewModel.myEpisodes.toString(), viewModel.mySubEpisodes.toString())
                    }
                    applyPreviousState()
                },
                onError = {
                    view.showLoadingFail()
                    route.close()
                }
        )
    }

    private fun applyPreviousState() {

        previousState?.let { state ->
            startDate = state.startDate
            finishDate = state.finishDate
            startDate?.let {date ->
                val formattedStartDate = reformatDate(date)
                if (formattedStartDate != null) {
                    view.showStartDate(formattedStartDate)
                } else {
                    view.showUnknownStart()
                }
            }
            finishDate?.let {date ->
                val formattedFinishDate = reformatDate(date)
                if (formattedFinishDate != null) {
                    view.showFinishDate(formattedFinishDate)
                } else {
                    view.showUnknownFinish()
                }
            }
            view.setupReChangeableInfo(state.isRe, state.reTimes, state.episodes, state.subEpisodes)
        }
    }

    private fun reformatDate(date: String): String? {
        val timestamp = dateFormatter.parse(date)?.time
        return if (timestamp == null) null else viewFormatter.format(timestamp)
    }

    fun selectStartDate() = currentRecord.let { record ->
        val startDate = record.myStartDate
        val dateForPicker = if (startDate != null)
            dateFormatter.parse(startDate)?.time ?: Date().time else Date().time
        route.openDatePicker(dateForPicker, DateType.START)
    }

    fun selectFinishDate() = currentRecord.let { record ->
        val finishDate = record.myFinishDate
        val dateForPicker = if (finishDate != null)
            dateFormatter.parse(finishDate)?.time ?: Date().time else Date().time
        route.openDatePicker(dateForPicker, DateType.FINISH)
    }

    fun setNewDate(type: DateType, year: Int, month: Int, dayOfMonth: Int) {

        val previousValue = if (type == DateType.START)
            currentRecord.myStartDate else currentRecord.myFinishDate

        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }
        val timestamp = calendar.timeInMillis
        val label = viewFormatter.format(timestamp)
        val value = dateFormatter.format(timestamp)

        if (type == DateType.START) {
            if (value != previousValue) {
                startDate = value
            }
            view.showStartDate(label)
        } else {
            if (value != previousValue) {
                finishDate = value
            }
            view.showFinishDate(label)
        }
    }

    fun setRewatching(isChecked: Boolean) {
        val episodes = if (isChecked) "0" else currentRecord.seriesEpisodes.toString()
        val subEpisodes = if (isChecked) "0" else currentRecord.seriesSubEpisodes.toString()
        view.setEpisodes(episodes, subEpisodes)
    }

    fun updateTitle(isRe: Boolean, reTimes: String, reEpisodes: String, reSubEpisodes: String) {

        var wasAnyChangesToRecordMade = false

        val updateParams = UpdateParams()
        if (startDate != null && startDate != currentRecord.myStartDate) {
            updateParams.startDate = startDate
            wasAnyChangesToRecordMade = true
        }
        if (finishDate != null && finishDate != currentRecord.myFinishDate) {
            updateParams.finishDate = finishDate
            wasAnyChangesToRecordMade = true
        }

        if (currentRecord.myStatus != Status.COMPLETED && !currentRecord.myRe) {
            val params = if (wasAnyChangesToRecordMade) updateParams else null
            route.returnResult(params)
            return
        }

        val enteredEpisodes = validateEpisodes(reEpisodes, currentRecord.seriesEpisodes)
        val enteredSubEpisodes = validateEpisodes(reSubEpisodes, currentRecord.seriesSubEpisodes)
        val enteredReTimes = if (reTimes.isNotEmpty()) Integer.valueOf(reTimes) else -1

        if (enteredReTimes == -1 || enteredEpisodes == -1 || (titleType == TitleType.MANGA && enteredSubEpisodes == -1)) {
            view.showEnteredValuesNotValid()
        } else {

            if (isRe != currentRecord.myRe) {
                if (titleType == TitleType.ANIME) {
                    updateParams.reWatching = isRe
                } else {
                    updateParams.reReading = isRe
                }
                wasAnyChangesToRecordMade = true
            }

            if (enteredReTimes != currentRecord.myReTimes) {
                if (titleType == TitleType.ANIME) {
                    updateParams.reWatchedTimes = enteredReTimes
                } else {
                    updateParams.reReadTimes = enteredReTimes
                }
                wasAnyChangesToRecordMade = true
            }

            if (enteredEpisodes != currentRecord.myEpisodes) {
                if (titleType == TitleType.ANIME) {
                    updateParams.episodes = enteredEpisodes
                } else {
                    updateParams.chapters = enteredEpisodes
                }
                wasAnyChangesToRecordMade = true
            }

            if (enteredSubEpisodes != currentRecord.mySubEpisodes && titleType == TitleType.MANGA) {
                updateParams.volumes = enteredSubEpisodes
                wasAnyChangesToRecordMade = true
            }

            val params = if (wasAnyChangesToRecordMade) updateParams else null
            route.returnResult(params)
        }
    }

    private fun validateEpisodes(enteredEpisodes: String, maxEpisode: Int) =
            try {
                if (enteredEpisodes.isNotEmpty() && (maxEpisode == 0 || Integer.valueOf(enteredEpisodes) <= maxEpisode)) {
                    Integer.valueOf(enteredEpisodes)
                } else {
                    -1
                }
            } catch (e: Exception) {
                -1
            }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        getRecordFromDbInteractor.cancel()
    }
}
