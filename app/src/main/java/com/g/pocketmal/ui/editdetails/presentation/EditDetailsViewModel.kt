package com.g.pocketmal.ui.editdetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.repository.RecordRepository
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.exception.RecordNotFoundException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDetailsViewModel @Inject constructor(
    val repository: RecordRepository,
    val converter: RecordExtraDetailsConverter,
) : ViewModel() {

    private val _recordDetailsState =
        MutableStateFlow<EditDetailsState>(EditDetailsState.Loading)
    val recordDetailsState = _recordDetailsState.asStateFlow()

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
}
