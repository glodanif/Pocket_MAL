package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.database.converter.TitleDetailsDataConverter
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.datasource.TitleDetailsDataSource
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.database.model.TitleDetailsTable
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.exception.EmptyResponseException

class GetTitleDetailsInteractor(
        private val apiService: ApiService,
        private val recordStorage: RecordDataSource,
        private val detailsStorage: TitleDetailsDataSource,
        private val converter: TitleDetailsDataConverter
) : BaseCachedNetworkCallInteractor<GetTitleDetailsInteractor.Params, GetTitleDetailsInteractor.Result>() {

    override suspend fun executeCache(input: Params): Result {

        val details =
                detailsStorage.getTitleDetailsById(input.recordId, input.titleType)

        if (details != null) {
            return Result(null, details)
        } else {
            throw EmptyResponseException("No title details was found with id=${input.recordId} (${input.titleType}) in db")
        }
    }

    override suspend fun executeNetwork(input: Params): Result {

        val response = apiService.getTitleDetails(input.recordId, input.titleType)

        val detailsResponse = response.body()
        if (response.isSuccessful && detailsResponse != null) {

            val dbEntry = converter.transform(detailsResponse, input.titleType)
            detailsStorage.saveTitleDetails(dbEntry)

            var record: DbListRecord? = null
            if (detailsResponse.myListStatus != null) {
                record = converter.transformToRecord(detailsResponse, input.titleType)
                recordStorage.overrideRecord(record)
            }

            return Result(record, dbEntry)
        } else {
            throw NetworkException(response.code(), errorMessage = "Getting user profile request was not successful")
        }
    }

    class Params(
            val recordId: Int,
            val titleType: TitleType
    )

    //FIXME?
    class Result(
           val record: DbListRecord?,
           val details: TitleDetailsTable
    )
}