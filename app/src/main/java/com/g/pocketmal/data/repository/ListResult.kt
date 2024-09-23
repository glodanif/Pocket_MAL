package com.g.pocketmal.data.repository

sealed class ListResult {
    data class RecordsList(val listStatus: ListStatus) : ListResult()
    data class Error(val throwable: Throwable) : ListResult()
}
