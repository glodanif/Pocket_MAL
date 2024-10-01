package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.model.DbListRecord

class RecordDataSourceImpl(listDbStorage: ListDbStorage) : RecordDataSource {

    private val recordDao = listDbStorage.db.recordDao()

    override suspend fun getRecordById(id: Int, type: DataTitleType): DbListRecord {
        return recordDao.getRecordById(id, type)
    }

    override suspend fun getRecordsByType(type: DataTitleType): List<DbListRecord> {
        return recordDao.getRecordsByType(type)
    }

    override suspend fun getRecordsByStatus(status: Int, type: DataTitleType): List<DbListRecord> {
        return recordDao.getRecordsByStatus(status, type)
    }

    override suspend fun saveRecord(record: DbListRecord) {
        recordDao.insert(record)
    }

    override suspend fun saveRecords(records: List<DbListRecord>) {
        recordDao.insert(records)
    }

    override suspend fun overrideRecord(record: DbListRecord) {
        recordDao.deleteById(record.seriesId, record.titleType)
        saveRecord(record)
    }

    override suspend fun deleteRecord(id: Int, type: DataTitleType) {
        recordDao.deleteById(id, type)
    }

    override suspend fun deleteRecordsByType(type: DataTitleType) {
        recordDao.deleteAllByType(type)
    }

    override suspend fun dropTable() {
        recordDao.dropTable()
    }
}
