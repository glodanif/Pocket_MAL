package com.g.pocketmal.domain.repository

import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.entity.ListRecord

interface RecordRepository {
    suspend fun getRecordFromLocalStorage(recordId: Int, titleType: TitleType): ListRecord
    suspend fun dropAllRecords()
}
