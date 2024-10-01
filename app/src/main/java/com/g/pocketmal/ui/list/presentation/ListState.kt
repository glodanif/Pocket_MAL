package com.g.pocketmal.ui.list.presentation

import androidx.annotation.StringRes
import com.g.pocketmal.domain.InListStatus
import com.g.pocketmal.domain.ListCounts

sealed class ListState {
    data class RecordsList(
        val list: List<RecordListViewEntity>,
        val status: InListStatus,
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
