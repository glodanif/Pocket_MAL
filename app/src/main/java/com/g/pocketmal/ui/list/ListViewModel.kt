package com.g.pocketmal.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.common.RecordsSubList
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.repository.ListRepository
import com.g.pocketmal.data.repository.ListStatus
import com.g.pocketmal.domain.RecordLists
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.util.list.DataInterpreter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val listRepository: ListRepository,
    private val converter: ListRecordConverter,
) : ViewModel() {

    private val _listState = MutableStateFlow<ListState>(ListState.Initial)
    val listState = _listState.asStateFlow()

    private val _recordsState = MutableStateFlow<ListStatus?>(null)

    private var currentStatus: Status = Status.IN_PROGRESS

    fun watchRecords(titleType: TitleType) {
        viewModelScope.launch {
            if (titleType.isAnime()) {
                listRepository.animeRecordsState.collect { status ->
                    val statusLabel = DataInterpreter.getStatusById(currentStatus, titleType)
                    _recordsState.value = status
                    _listState.value = ListState.RecordsList(
                        list = getRecordsByStatus(titleType, currentStatus),
                        status = currentStatus,
                        statusLabel = statusLabel,
                        counts = status.getCounts(),
                        isSynchronizing = status.isListSynchronizing,
                        isSynchronized = status.isListSynchronized,
                        isPreloaded = status.isListFetchedFromDb,
                        synchronizationError = status.syncError,
                        synchronizedAt = status.syncAt.toString(),
                    )
                }
            }
        }
    }

    fun synchronizeList(titleType: TitleType) {
        viewModelScope.launch {
            listRepository.loadRecords(titleType)
        }
    }

    fun switchStatus(titleType: TitleType, status: Status) {
        currentStatus = status
        val statusLabel = DataInterpreter.getStatusById(currentStatus, titleType)
        val state = _listState.value
        if (state is ListState.RecordsList) {
            _listState.value = state.copy(
                list = getRecordsByStatus(titleType, currentStatus),
                status = currentStatus,
                statusLabel = statusLabel,
            )
        }
    }

    private fun getRecordsByStatus(
        titleType: TitleType,
        status: Status,
    ): List<RecordListViewEntity> {
        val records = _recordsState.value?.lists ?: RecordLists()
        val list = when (status) {
            Status.IN_PROGRESS -> records.inProgress
            Status.COMPLETED -> records.completed
            Status.ON_HOLD -> records.onHold
            Status.DROPPED -> records.dropped
            Status.PLANNED -> records.planned
            else -> RecordsSubList()
        }
        return converter.transform(titleType, list.list, false)
    }
}
