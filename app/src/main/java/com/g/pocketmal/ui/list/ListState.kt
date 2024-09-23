package com.g.pocketmal.ui.list

import androidx.annotation.StringRes
import com.g.pocketmal.data.common.ListCounts
import com.g.pocketmal.data.common.Status

sealed class ListState {
    data class RecordsList(
        val list: List<RecordListViewEntity>,
        val status: Status,
        @StringRes val statusLabel: Int,
        val counts: ListCounts,
        val isSynchronizing: Boolean,
        val synchronizationError: SynchronizationError? = null,
    ) : ListState()

    data class Error(
        val message: String,
    ) : ListState()

    data object Loading : ListState()
    data object Initial : ListState()
}