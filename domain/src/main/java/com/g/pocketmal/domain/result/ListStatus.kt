package com.g.pocketmal.domain.result

import com.g.pocketmal.domain.ListCounts
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
    val counts: ListCounts = ListCounts(),
)
