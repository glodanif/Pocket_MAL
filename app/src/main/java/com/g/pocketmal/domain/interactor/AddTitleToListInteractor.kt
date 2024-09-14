package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.api.response.ErrorResponse
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.datasource.TitleDetailsDataSource
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.data.converter.ListRecordEntityConverter
import com.g.pocketmal.domain.exception.NotApprovedTitleException
import com.google.gson.Gson

//FIXME use list entity as a return type
class AddTitleToListInteractor(
        private val apiService: ApiService,
        private val recordStorage: RecordDataSource,
        private val detailsStorage: TitleDetailsDataSource,
        private val converter: ListRecordEntityConverter
) : BaseInteractor<AddTitleToListInteractor.Params, DbListRecord>() {

    private val invalidContent = "invalid_content"

    override suspend fun execute(input: Params): DbListRecord {

        val details = detailsStorage.getTitleDetailsById(input.recordId, input.titleType)
        require(details != null) { "Title info was not found in db" }

        val response = apiService.addTitle(input.recordId, input.titleType)

        val addingResponse = response.body()
        if (response.isSuccessful && addingResponse != null) {
            val justAddedDbRecord = converter.transform(details)
            recordStorage.saveRecord(justAddedDbRecord)
            return justAddedDbRecord
        } else {

            val errorResponse = response.errorBody()
            if (errorResponse != null) {
                val body = errorResponse.string()
                val error = Gson().fromJson(body, ErrorResponse::class.java)
                if (error.error != null && error.error == invalidContent) {
                    throw NotApprovedTitleException(error.message ?: "")
                }
            }

            throw NetworkException(response.code(), errorMessage = "Adding request was not successful")
        }
    }

    class Params(
            val recordId: Int,
            val titleType: TitleType
    )
}
