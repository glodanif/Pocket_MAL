package com.g.pocketmal.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.repository.ListRepository
import com.g.pocketmal.data.repository.ListResult
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.legacy.viewentity.converter.ListRecordConverter
import com.g.pocketmal.util.list.ListsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val listRepository: ListRepository,
    private val listsManager: ListsManager,
    private val converter: ListRecordConverter,
) : ViewModel() {

    private val _listState = MutableStateFlow<ListState>(ListState.Initial)
    val listState = _listState.asStateFlow()

    fun loadList(titleType: TitleType, status: Status = Status.IN_PROGRESS) {
        _listState.value = ListState.RecordsList(emptyList())

        viewModelScope.launch {

            val list = listsManager.getListByStatus(status, titleType)
            val dbEntities = converter.transform(titleType, list, false)
            _listState.value = ListState.RecordsList(dbEntities)

            val response = listRepository.loadListFromNetwork(titleType)
            when (response) {
                is ListResult.RecordsList -> {
                    val networkEntities = converter.transform(titleType, list, false)
                    _listState.value = ListState.RecordsList(networkEntities)
                }
                is ListResult.Error -> {
                    _listState.value = ListState.Error(response.throwable.message?: "")
                }
            }
        }
    }
}
