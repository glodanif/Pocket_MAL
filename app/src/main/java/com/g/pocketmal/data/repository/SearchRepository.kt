package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.converter.SearchEntityConverter

class SearchRepository(
    private val apiService: ApiService,
    private val settings: MainSettings,
    private val converter: SearchEntityConverter,
) {

    private val minCharactersForQuery = 3

    suspend fun search(query: String, titleType: TitleType): SearchResult {

        if (query.length < minCharactersForQuery) {
            return SearchResult.InvalidQuery(minCharactersForQuery)
        }

        val includeNsfw = settings.displayNsfw()

        val response = apiService.search(query, titleType, includeNsfw)
        val searchResponse = response.body()

        if (response.isSuccessful && searchResponse != null) {
            val useEnglishTitles = settings.showEnglishTitles()
            val result = converter.transform(searchResponse, useEnglishTitles)
            return if (result.isNotEmpty()) {
                SearchResult.Result(result)
            } else {
                SearchResult.EmptyResult
            }
        } else {
            return SearchResult.NetworkError(
                message = response.message(),
                code = response.code(),
            )
        }
    }
}
