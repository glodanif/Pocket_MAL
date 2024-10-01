package com.g.pocketmal.data.repository

import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.data.converter.ListRecordEntityConverter
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.entity.ListRecord
import com.g.pocketmal.domain.exception.RecordNotFoundException
import com.g.pocketmal.domain.repository.RecordRepository

class RecordRepositoryImplementation(
    private val recordStorage: RecordDataSource,
    private val converter: ListRecordEntityConverter,
) : RecordRepository {

    override suspend fun getRecordFromLocalStorage(
        recordId: Int,
        titleType: TitleType
    ): ListRecord {
        val record = recordStorage.getRecordById(recordId, DataTitleType.Companion.from(titleType))
            ?: throw RecordNotFoundException("Record $recordId | $titleType is not present in the DB")
        return converter.transform(record)
    }

    override suspend fun dropAllRecords() {
        recordStorage.dropTable()
    }
}
