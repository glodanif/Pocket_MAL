package com.g.pocketmal.data.repository

import android.util.Log
import com.g.pocketmal.data.converter.ListRecordEntityConverter
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.ListRecordEntity
import com.g.pocketmal.domain.exception.RecordNotFoundException

class RecordRepository(
    private val recordStorage: RecordDataSource,
    private val converter: ListRecordEntityConverter,
) {

    suspend fun getRecordFromLocalStorage(recordId: Int, titleType: TitleType): ListRecordEntity {
        val record = recordStorage.getRecordById(recordId, titleType)
            ?: throw RecordNotFoundException("Record $recordId | $titleType is not present in the DB")
        return converter.transform(record)
    }
}
