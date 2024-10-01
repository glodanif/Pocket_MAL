package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.converter.SearchEntityConverter
import com.g.pocketmal.data.keyvalue.UserSettingsStorage
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.repository.SearchRepository
import com.g.pocketmal.domain.result.SearchResult

class SearchRepositoryImplementation(
    private val apiService: ApiService,
    private val settings: UserSettingsStorage,
    private val converter: SearchEntityConverter,
) : SearchRepository {

    override suspend fun search(query: String, titleType: TitleType): SearchResult {

        val minCharactersForQuery = getMinCharactersForQuery()

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
