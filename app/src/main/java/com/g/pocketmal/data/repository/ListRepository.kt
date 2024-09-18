package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.api.response.ErrorResponse
import com.g.pocketmal.data.api.response.ListItem
import com.g.pocketmal.data.database.converter.ListRecordDataConverter
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.exception.ListAccessException
import com.g.pocketmal.domain.exception.MalDownException
import com.g.pocketmal.util.list.ListsManager
import com.google.gson.Gson

class ListRepository(
    private val apiService: ApiService,
    private val converter: ListRecordDataConverter,
    private val localStorage: LocalStorage,
    private val settings: UserSettings,
    private val recordStorage: RecordDataSource,
    private val listsManager: ListsManager,
) {

    private val notPermitted = "not_permitted"

    suspend fun loadListFromDb() {
        val dbAnimeList = recordStorage.getRecordsByType(TitleType.ANIME)
        val dbMangaList = recordStorage.getRecordsByType(TitleType.MANGA)
        if (dbAnimeList != null) {
            listsManager.initAnimeLists(dbAnimeList)
        }
        if (dbMangaList != null) {
            listsManager.initMangaLists(dbMangaList)
        }
    }

    suspend fun loadListFromNetwork(titleType: TitleType): ListResult {

        val includeNsfw = settings.getDisplayNsfwInList()

        val list = ArrayList<ListItem>()

        val response = apiService.getFirstListPage(titleType, includeNsfw)
        val body = response.body()
        if (response.isSuccessful && body != null) {

            list.addAll(body.list)

            val next = body.pagingInfo.next
            if (next != null) {
                list.addAll(loadNextPage(next, list))
            }

            val result = list.map { record ->
                converter.transform(record, titleType)
            }

            recordStorage.deleteRecordsByType(titleType)
            recordStorage.saveRecords(result)
            localStorage.storeLastSynchronizing(System.currentTimeMillis(), titleType)

            return ListResult.RecordsList(result)

        } else {

            val errorResponse = response.errorBody()
            if (errorResponse != null) {
                val errorBody = errorResponse.string()
                val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                if (error.error != null && error.error == notPermitted) {
                    val throwable = ListAccessException(error.message ?: "")
                    return ListResult.Error(throwable)
                }
            }

            val errorCode = response.code()
            if (errorCode in 500..599) {
                val throwable =  MalDownException(errorMessage = "Server side problem, code = $errorCode")
                return ListResult.Error(throwable)
            } else {
                val throwable =  NetworkException(errorCode, errorMessage = "List request was not successful")
                return ListResult.Error(throwable)
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
