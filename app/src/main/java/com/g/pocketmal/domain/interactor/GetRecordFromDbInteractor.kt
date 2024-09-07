package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.util.TitleType

class GetRecordFromDbInteractor(
        private val recordStorage: RecordDataSource
) : BaseInteractor<GetRecordFromDbInteractor.Params, DbListRecord>() {

    override suspend fun execute(input: Params): DbListRecord {

        val record = recordStorage.getRecordById(input.recordId, input.titleType)
        require(record != null) { "Record was not found in db" }
        return record
    }

    class Params(
            val recordId: Int,
            val titleType: TitleType
    )
}