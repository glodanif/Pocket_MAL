package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.data.converter.SearchEntityConverter

class SearchRepository(
    private val apiService: ApiService,
    private val settings: UserSettings,
    private val converter: SearchEntityConverter,
) {

    private val minCharactersForQuery = 3

    suspend fun search(query: String, titleType: TitleType): SearchResult {

        if (query.length < minCharactersForQuery) {
            return SearchResult.InvalidQuery(minCharactersForQuery)
        }

        val includeNsfw = settings.getDisplayNsfw()

        val response = apiService.search(query, titleType, includeNsfw)
        val searchResponse = response.body()

        if (response.isSuccessful && searchResponse != null) {
            val useEnglishTitles = settings.getShowEnglishTitles()
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
