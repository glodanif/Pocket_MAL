package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.domain.TitleType
import kotlinx.coroutines.flow.Flow

interface RecordDataSource {
    suspend fun getRecordById(id: Int, type: TitleType): DbListRecord?
    suspend fun getRecordsByType(type: TitleType): List<DbListRecord>?
    suspend fun getRecordsByStatus(status: Int, type: TitleType): List<DbListRecord>?
    suspend fun saveRecord(record: DbListRecord)
    suspend fun saveRecords(records: List<DbListRecord>)
    suspend fun overrideRecord(record: DbListRecord)
    suspend fun deleteRecord(id: Int, type: TitleType)
    suspend fun deleteRecordsByType(type: TitleType)
    suspend fun dropTable()
}
