package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.api.response.ErrorResponse
import com.g.pocketmal.data.api.response.ListItem
import com.g.pocketmal.data.common.RecordComparator
import com.g.pocketmal.data.common.RecordsSubList
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.database.converter.ListRecordDataConverter
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.domain.RecordLists
import com.g.pocketmal.domain.SortingOptions
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.exception.ListAccessException
import com.g.pocketmal.domain.exception.MalDownException
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class ListRepository(
    private val apiService: ApiService,
    private val converter: ListRecordDataConverter,
    private val localStorage: LocalStorage,
    private val settings: UserSettings,
    private val recordStorage: RecordDataSource,
) {
    private val notPermitted = "not_permitted"

    private val _animeRecordsState = MutableStateFlow(ListStatus())
    val animeRecordsState = _animeRecordsState.asStateFlow()
    private val _mangaRecordsState = MutableStateFlow(ListStatus())
    val mangaRecordsState = _mangaRecordsState.asStateFlow()

    private var sortingOptions: SortingOptions = SortingOptions()

    suspend fun loadRecords(type: TitleType) {
        loadRecordsFromDb(type)
        loadListFromNetwork(type)
    }

    private suspend fun loadRecordsFromDb(type: TitleType) {
        recordStorage.getRecordsByType(type)?.let { list ->
            val usePreviousSorting = settings.isSaveSortingOrderEnabled()
            if (usePreviousSorting) {
                sortingOptions = SortingOptions(
                    localStorage.getSortingType(),
                    localStorage.getSortingReverse()
                )
            }
            if (type.isAnime()) {
                _animeRecordsState.value = prepareList(list)
            } else {
                _mangaRecordsState.value = prepareList(list)
            }
        }
    }

    private suspend fun loadListFromNetwork(titleType: TitleType) {

        if (titleType.isAnime()) {
            _animeRecordsState.value = _animeRecordsState.value.copy(
                isListSynchronizing = true,
                isListSynchronized = false,
            )
        } else {
            _mangaRecordsState.value = _mangaRecordsState.value.copy(
                isListSynchronizing = true,
                isListSynchronized = false,
            )
        }

        val list = ArrayList<ListItem>()
        try {
            val response = apiService.getFirstListPage(titleType)
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

                localStorage.storeLastSynchronizing(System.currentTimeMillis(), titleType)
                recordStorage.deleteRecordsByType(titleType)
                recordStorage.saveRecords(result)

                if (titleType.isAnime()) {
                    _animeRecordsState.value = prepareList(result).copy(
                        isListSynchronizing = false,
                        isListSynchronized = true,
                        isListFetchedFromDb = true,
                    )
                } else {
                    _mangaRecordsState.value = prepareList(result).copy(
                        isListSynchronizing = false,
                        isListSynchronized = true,
                        isListFetchedFromDb = true,
                    )
                }
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
                    throw NetworkException(
                        errorCode,
                        errorMessage = "List request was not successful"
                    )
                }
            }
        } catch (e: Exception) {
            if (titleType.isAnime()) {
                _animeRecordsState.value = _animeRecordsState.value.copy(
                    isListSynchronizing = false,
                    isListSynchronized = false,
                    syncError = e.message ?: "error",
                    syncAt = Date(localStorage.getLastSynchronizing(titleType))
                )
            } else {
                _mangaRecordsState.value = _mangaRecordsState.value.copy(
                    isListSynchronizing = false,
                    isListSynchronized = false,
                    syncError = e.message ?: "error",
                    syncAt = Date(localStorage.getLastSynchronizing(titleType))
                )
            }
        }
    }

    private fun prepareList(list: List<DbListRecord>): ListStatus {
        val includeNsfw = settings.getDisplayNsfwInList()
        val comparator = RecordComparator(sortingOptions.type, sortingOptions.reverse)
        val lists = RecordLists(
            inProgress = list.sortedWith(comparator)
                .filterByStatus(Status.IN_PROGRESS, includeNsfw),
            completed = list.sortedWith(comparator).filterByStatus(Status.COMPLETED, includeNsfw),
            onHold = list.sortedWith(comparator).filterByStatus(Status.ON_HOLD, includeNsfw),
            dropped = list.sortedWith(comparator).filterByStatus(Status.DROPPED, includeNsfw),
            planned = list.sortedWith(comparator).filterByStatus(Status.PLANNED, includeNsfw),
        )
        return ListStatus(
            lists = lists,
            sortingOptions = sortingOptions,
            isListFetchedFromDb = list.isNotEmpty()
        )
    }

    private suspend fun loadNextPage(
        nextUrl: String,
        list: ArrayList<ListItem>
    ): ArrayList<ListItem> {

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

    private fun List<DbListRecord>.filterByStatus(
        status: Status,
        includeNsfw: Boolean,
    ): RecordsSubList {

        var quanity = 0
        val list = this.filter { element ->
            when {
                !includeNsfw && element.seriesNsfw == "black" -> false
                status == Status.GENERAL -> {
                    quanity++
                    true
                }

                status == Status.IN_PROGRESS -> {
                    if (element.myStatus == status) {
                        quanity++
                    }
                    element.myStatus == status || element.myRe
                }

                else -> {
                    if (element.myStatus == status) {
                        quanity++
                    }
                    element.myStatus == status
                }
            }
        }

        return RecordsSubList(list = list, quantity = quanity)
    }

    fun clearInstance() {
        _animeRecordsState.value = ListStatus()
        _mangaRecordsState.value = ListStatus()
    }
}
