package com.g.pocketmal.ui.editdetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.repository.RecordRepository
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.exception.RecordNotFoundException
import com.g.pocketmal.domain.unsetDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditDetailsViewModel @Inject constructor(
    val repository: RecordRepository,
    val converter: RecordExtraDetailsConverter,
) : ViewModel() {

    private val _recordDetailsState =
        MutableStateFlow<EditDetailsState>(EditDetailsState.Loading)
    val recordDetailsState = _recordDetailsState.asStateFlow()

    val updateParameters = UpdateParams()

    fun loadRecordDetails(recordId: Int, titleType: TitleType) {
        if (recordId <= 0) {
            _recordDetailsState.value = EditDetailsState.IncorrectInput
            return
        }

        viewModelScope.launch {
            try {
                val record = repository.getRecordFromLocalStorage(recordId, titleType)
                val viewEntity = converter.transform(record)
                _recordDetailsState.value = EditDetailsState.RecordDetails(viewEntity)
            } catch (exception: RecordNotFoundException) {
                _recordDetailsState.value = EditDetailsState.NotFound
            }
        }
    }

    fun updateStartDate(newStartDate: Long?) {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails && newStartDate != null) {
            val updatedDetails = state.details.copy(
                startDate = Date(newStartDate),
                startDateFormatted = converter.timestampToViewDate(newStartDate)
            )
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            updateParameters.startDate = converter.timestampToDomainDate(newStartDate)
        }
    }

    fun removeStartDate() {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails) {
            val updatedDetails = state.details.copy(
                startDate = null,
                startDateFormatted = null,
            )
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            updateParameters.startDate = unsetDate
        }
    }

    fun updateFinishDate(newFinishDate: Long?) {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails && newFinishDate != null) {
            val updatedDetails = state.details.copy(
                finishDate = Date(newFinishDate),
                finishDateFormatted = converter.timestampToViewDate(newFinishDate)
            )
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            updateParameters.finishDate = converter.timestampToDomainDate(newFinishDate)
        }
    }

    fun removeFinishDate() {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails) {
            val updatedDetails = state.details.copy(
                finishDate = null,
                finishDateFormatted = null,
            )
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            updateParameters.finishDate = unsetDate
        }
    }

    fun updateRe(re: Boolean, titleType: TitleType) {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails) {
            val updatedDetails = state.details.copy(
                isRe = re,
                myEpisodes = if (re) 1 else state.details.seriesEpisodes,
                mySubEpisodes = if (re) 0 else state.details.seriesSubEpisodes,
            )
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            if (titleType == TitleType.ANIME) {
                updateParameters.reWatching = re
                updateParameters.episodes = if (re) 1 else state.details.seriesEpisodes
            } else {
                updateParameters.reReading = re
                updateParameters.chapters = if (re) 1 else state.details.seriesEpisodes
                updateParameters.volumes = if (re) 0 else state.details.seriesSubEpisodes
            }
        }
    }

    fun updateReTimes(reTimes: Int?, titleType: TitleType) {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails) {
            val updatedDetails = state.details.copy(reTimes = reTimes)
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            if (titleType == TitleType.ANIME) {
                updateParameters.reWatchedTimes = reTimes ?: 0
            } else {
                updateParameters.reReadTimes = reTimes ?: 0
            }
        }
    }

    fun updateReEpisodes(reEpisodes: Int?, titleType: TitleType) {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails) {
            val updatedDetails = state.details.copy(myEpisodes = reEpisodes)
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            if (titleType == TitleType.ANIME) {
                updateParameters.episodes = reEpisodes ?: 0
            } else {
                updateParameters.chapters = reEpisodes ?: 0
            }
        }
    }

    fun updateReSubEpisodes(reSubEpisodes: Int?, titleType: TitleType) {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails) {
            val updatedDetails = state.details.copy(mySubEpisodes = reSubEpisodes)
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            if (titleType == TitleType.MANGA) {
                updateParameters.volumes = reSubEpisodes ?: 0
            }
        }
    }

    fun updateReValue(reValue: Int, titleType: TitleType) {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails) {
            val updatedDetails = state.details.copy(reValue = reValue)
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            if (titleType == TitleType.ANIME) {
                updateParameters.reWatchValue = reValue
            } else {
                updateParameters.reReadValue = reValue
            }
        }
    }

    fun updateComments(comments: String) {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails) {
            val updatedDetails = state.details.copy(comments = comments)
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            updateParameters.comments = comments
        }
    }

    fun updatePriority(priority: Int) {
        val state = recordDetailsState.value
        if (state is EditDetailsState.RecordDetails) {
            val updatedDetails = state.details.copy(priority = priority)
            _recordDetailsState.value = EditDetailsState.RecordDetails(details = updatedDetails)
            updateParameters.priority = priority
        }
    }
}
