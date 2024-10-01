package com.g.pocketmal.domain.result

import com.g.pocketmal.domain.entity.SearchResultTitle

sealed class SearchResult {
    data class Result(val searchResult: List<SearchResultTitle>) : SearchResult()
    data object EmptyResult : SearchResult()
    data class InvalidQuery(val minCharacters: Int) : SearchResult()
    data class NetworkError(val message: String, val code: Int) : SearchResult()
}
