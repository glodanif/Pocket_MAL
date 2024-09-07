package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.util.Season
import com.g.pocketmal.domain.entity.SeasonEntity
import com.g.pocketmal.data.converter.SeasonEntityConverter
import com.g.pocketmal.domain.exception.EmptyResponseException

class GetSeasonalAnimeInteractor(
        private val apiService: ApiService,
        private val settings: MainSettings,
        private val converter: SeasonEntityConverter
) : BaseInteractor<Season, List<SeasonEntity>>() {

    override suspend fun execute(input: Season): List<SeasonEntity> {

        val includeNsfw = settings.displayNsfw()

        val response = apiService.getSeasonalAnime(input.partOfYear, input.year, includeNsfw)
        val seasonalResponse = response.body()
        if (response.isSuccessful && seasonalResponse != null) {

            val useEnglishTitles = settings.showEnglishTitles()
            val items = converter.transform(seasonalResponse, useEnglishTitles)
            if (items.isNotEmpty()) {
                return items
            } else {
                throw EmptyResponseException("No anime was found in ${input.partOfYear.season} ${input.year} season")
            }
        } else {
            if (response.code() == 404) {
                throw EmptyResponseException("No anime was found in ${input.partOfYear.season} ${input.year} season")
            } else {
                throw NetworkException(response.code(), errorMessage = "Season request is not successful")
            }
        }
    }
}
