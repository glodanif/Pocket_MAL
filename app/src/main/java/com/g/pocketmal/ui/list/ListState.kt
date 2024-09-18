package com.g.pocketmal.ui.list

import com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel

sealed class ListState {
    data class RecordsList(val list: List<RecordListViewModel>) : ListState()
    data class Error(val message: String) : ListState()
    data object Loading : ListState()
    data object Initial : ListState()
}