package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.MalApi
import com.g.pocketmal.data.converter.RankingEntityConverter
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.domain.TitleType

class BrowseRepository(
    private val apiService: ApiService,
    private val converter: RankingEntityConverter,
    private val userSettings: UserSettings,
) {

    suspend fun loadRankedTitles(
        titleType: TitleType,
        rankingType: RankingType,
        offset: Int,
    ): BrowseResult {

        val withNsfw = userSettings.getDisplayNsfw()
        val useEnglishTitle = userSettings.getShowEnglishTitles()

        val response = apiService.getRankingList(
            titleType,
            rankingType,
            withNsfw,
            MalApi.BROWSE_PAGE_LIMIT,
            offset,
        )

        val body = response.body()

        if (response.isSuccessful && body != null) {
            val result = converter.transform(body, titleType, useEnglishTitle)
            return if (body.list.isNotEmpty()) {
                BrowseResult.Result(result)
            } else {
                BrowseResult.EmptyResult
            }
        } else {
            return BrowseResult.NetworkError(
                message = response.message(),
                code = response.code(),
            )
        }
    }
}
