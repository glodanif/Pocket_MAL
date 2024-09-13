package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.api.response.ErrorResponse
import com.g.pocketmal.data.api.response.ListItem
import com.g.pocketmal.data.database.converter.ListRecordDataConverter
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.exception.ListAccessException
import com.g.pocketmal.domain.exception.MalDownException
import com.google.gson.Gson

class LoadListFromNetworkInteractor(
        private val apiService: ApiService,
        private val converter: ListRecordDataConverter,
        private val recordStorage: RecordDataSource,
        private val localStorage: LocalStorage,
        private val settings: UserSettings
) : BaseInteractor<TitleType, List<DbListRecord>>() {

    private val notPermitted = "not_permitted"

    override suspend fun execute(input: TitleType): List<DbListRecord> {

        val includeNsfw = settings.getDisplayNsfwInList()

        val list = ArrayList<ListItem>()

        val response = apiService.getFirstListPage(input, includeNsfw)
        val body = response.body()
        if (response.isSuccessful && body != null) {

            list.addAll(body.list)

            val next = body.pagingInfo.next
            if (next != null) {
                list.addAll(loadNextPage(next, list))
            }

            val result = list.map { record ->
                converter.transform(record, input)
            }

            recordStorage.deleteRecordsByType(input)
            recordStorage.saveRecords(result)
            localStorage.storeLastSynchronizing(System.currentTimeMillis(), input)

            return result

        } else {

            val errorResponse = response.errorBody()
            if (errorResponse != null) {
                val errorBody = errorResponse.string()
                val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                if (error.error != null && error.error == notPermitted) {
                    throw ListAccessException(error.message ?: "")
                }
            }

            val errorCode = response.code()
            if (errorCode in 500..599) {
                throw MalDownException(errorMessage = "Server side problem, code = $errorCode")
            } else {
                throw NetworkException(errorCode, errorMessage = "List request was not successful")
            }
        }
    }

    private suspend fun loadNextPage(nextUrl: String, list: ArrayList<ListItem>): ArrayList<ListItem> {

        val response = apiService.getListPage(nextUrl)
        val body = response.body()
        if (response.isSuccessful && body != null) {

            list.addAll(body.list)

            body.pagingInfo.next?.let {
                return@loadNextPage loadNextPage(it, list)
            }
            return list
        }

        val errorCode = response.code()
        if (errorCode in 500..599) {
            throw MalDownException(errorMessage = "Server side problem, code = $errorCode")
        } else {
            throw NetworkException(errorCode, "Getting list is not completed")
        }
    }
}
