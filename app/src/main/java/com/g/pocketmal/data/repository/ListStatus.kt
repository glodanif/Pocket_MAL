package com.g.pocketmal.data.repository

import com.g.pocketmal.data.common.ListCounts
import com.g.pocketmal.domain.RecordLists
import com.g.pocketmal.domain.SortingOptions
import java.util.Date

data class ListStatus(
    val lists: RecordLists = RecordLists(),
    val sortingOptions: SortingOptions = SortingOptions(),
    val isListFetchedFromDb: Boolean = false,
    val isListSynchronized: Boolean = false,
    val isListSynchronizing: Boolean = false,
    val syncError: String? = null,
    val syncAt: Date? = null,
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
        return "ListStatus(inProgress.list.size=${lists.inProgress.list.size}, isListSynchronizing=$isListSynchronizing, isListSynchronized=$isListSynchronized, isListFetchedFromDb=$isListFetchedFromDb)"
    }


}
