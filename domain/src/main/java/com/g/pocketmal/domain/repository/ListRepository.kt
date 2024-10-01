package com.g.pocketmal.domain.repository

import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.result.ListStatus
import kotlinx.coroutines.flow.StateFlow

interface ListRepository {
    val animeRecordsState: StateFlow<ListStatus>
    val mangaRecordsState: StateFlow<ListStatus>
    suspend fun loadRecords(type: TitleType)
    fun clearInstance()
}
