package com.g.pocketmal.data.repository

import com.g.pocketmal.data.common.ListCounts
import com.g.pocketmal.domain.RecordLists
import com.g.pocketmal.domain.SortingOptions

data class ListStatus(
    val lists: RecordLists = RecordLists(),
    val sortingOptions: SortingOptions = SortingOptions(),
    val isListSynchronized: Boolean = false,
    val isListSynchronizing: Boolean = false,
    val syncError: String? = null,
) {

    fun getCounts(): ListCounts {
        return ListCounts(
            inProgressCount = lists.inProgress.quantity,
            completedCount = lists.completed.quantity,
            onHoldCount = lists.onHold.quantity,
            droppedCount = lists.dropped.quantity,
            plannedCount = lists.planned.quantity,
        )
    }

    override fun toString(): String {
        return "ListStatus(sortingOptions=$sortingOptions, isListSynchronized=$isListSynchronized, isListSynchronizing=$isListSynchronizing, syncError=$syncError)"
    }
}
