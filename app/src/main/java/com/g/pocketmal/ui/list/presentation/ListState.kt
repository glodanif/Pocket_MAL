package com.g.pocketmal.ui.list.presentation

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
        val isSynchronized: Boolean,
        val isPreloaded: Boolean,
        val synchronizationError: String? = null,
        val synchronizedAt: String? = null,
    ) : ListState()

    data object Initial : ListState()
}
