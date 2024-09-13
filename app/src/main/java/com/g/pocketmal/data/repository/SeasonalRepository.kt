package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.converter.SeasonEntityConverter
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.data.util.PartOfYear

class SeasonalRepository(
    private val apiService: ApiService,
    private val converter: SeasonEntityConverter,
    private val settings: UserSettings,
) {

    suspend fun getSeasonalAnime(year: Int, season: PartOfYear): SeasonalResult {

        val includeNsfw = settings.getDisplayNsfw()

        val response = apiService.getSeasonalAnime(season, year, includeNsfw)
        val seasonalResponse = response.body()
        if (response.isSuccessful && seasonalResponse != null) {

            val useEnglishTitles = settings.getShowEnglishTitles()
            val items = converter.transform(seasonalResponse, useEnglishTitles)
            return if (items.isNotEmpty())
                SeasonalResult.Result(items) else SeasonalResult.EmptyResult

        } else {
            return if (response.code() == 404) SeasonalResult.EmptyResult else
                SeasonalResult.NetworkError(message = response.message(), code = response.code())
        }
    }
}
