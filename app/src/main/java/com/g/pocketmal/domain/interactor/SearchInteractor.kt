package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.SearchEntity
import com.g.pocketmal.domain.entity.converter.SearchEntityConverter
import com.g.pocketmal.domain.exception.EmptyResponseException
import java.lang.IllegalArgumentException

class SearchInteractor(
        private val apiService: ApiService,
        private val settings: MainSettings,
        private val converter: SearchEntityConverter
) : BaseInteractor<SearchInteractor.Params, List<SearchEntity>>() {

    private val minCharactersForQuery = 3

    override suspend fun execute(input: Params): List<SearchEntity> {

        if (input.query.length < minCharactersForQuery) {
            throw IllegalArgumentException("Query must contain at least $minCharactersForQuery characters")
        }

        val includeNsfw = settings.displayNsfw()

        val response = apiService.search(input.query, input.titleType, includeNsfw)
        val searchResponse = response.body()

        if (response.isSuccessful && searchResponse != null) {

            val useEnglishTitles = settings.showEnglishTitles()
            val result = converter.transform(searchResponse, useEnglishTitles)

            if (result.isEmpty()) {
                throw EmptyResponseException("Search result is empty")
            }

            return result
        } else {
            throw NetworkException(response.code(), errorMessage = "Search request was not successful")
        }
    }

    class Params(
            val query: String,
            val titleType: TitleType
    )
}
