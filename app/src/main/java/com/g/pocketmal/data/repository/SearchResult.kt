package com.g.pocketmal.data.repository

import com.g.pocketmal.domain.entity.SearchEntity

sealed class SearchResult {
    data class Result(val searchResult: List<SearchEntity>) : SearchResult()
    data object EmptyResult : SearchResult()
    data class InvalidQuery(val minCharacters: Int) : SearchResult()
    data class NetworkError(val message: String, val code: Int) : SearchResult()
}
