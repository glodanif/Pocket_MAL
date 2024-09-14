package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.domain.TitleType

class RemoveTitleFromListInteractor(
        private val apiService: ApiService,
        private val recordStorage: RecordDataSource
) : BaseInteractor<RemoveTitleFromListInteractor.Params, Unit>() {

    override suspend fun execute(input: Params) {

        val response = apiService.deleteTitle(input.recordId, input.titleType)

        if (response.isSuccessful) {
            recordStorage.deleteRecord(input.recordId, input.titleType)
        } else {
            throw NetworkException(response.code(), errorMessage = "Removing request was not successful")
        }
    }

    class Params(
            val recordId: Int,
            val titleType: TitleType
    )
}