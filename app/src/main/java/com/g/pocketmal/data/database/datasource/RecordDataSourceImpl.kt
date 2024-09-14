package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.domain.TitleType

class RecordDataSourceImpl(listDbStorage: ListDbStorage) : RecordDataSource {

    private val recordDao = listDbStorage.db.recordDao()

    override suspend fun getRecordById(id: Int, type: TitleType): DbListRecord {
        return recordDao.getRecordById(id, type)
    }

    override suspend fun getRecordsByType(type: TitleType): List<DbListRecord> {
        return recordDao.getRecordsByType(type)
    }

    override suspend fun getRecordsByStatus(status: Int, type: TitleType): List<DbListRecord> {
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

    override suspend fun deleteRecord(id: Int, type: TitleType) {
        recordDao.deleteById(id, type)
    }

    override suspend fun deleteRecordsByType(type: TitleType) {
        recordDao.deleteAllByType(type)
    }

    override suspend fun dropTable() {
        recordDao.dropTable()
    }
}
