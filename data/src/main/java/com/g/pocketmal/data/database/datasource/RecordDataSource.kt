package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.data.database.model.DbListRecord

interface RecordDataSource {
    suspend fun getRecordById(id: Int, type: DataTitleType): DbListRecord?
    suspend fun getRecordsByType(type: DataTitleType): List<DbListRecord>?
    suspend fun getRecordsByStatus(status: Int, type: DataTitleType): List<DbListRecord>?
    suspend fun saveRecord(record: DbListRecord)
    suspend fun saveRecords(records: List<DbListRecord>)
    suspend fun overrideRecord(record: DbListRecord)
    suspend fun deleteRecord(id: Int, type: DataTitleType)
    suspend fun deleteRecordsByType(type: DataTitleType)
    suspend fun dropTable()
}
