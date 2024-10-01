package com.g.pocketmal.data.repository

import com.g.pocketmal.data.DataExploreType
import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.MalApi
import com.g.pocketmal.data.converter.RankingEntityConverter
import com.g.pocketmal.data.keyvalue.UserSettingsStorage
import com.g.pocketmal.domain.ExploreType
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.repository.BrowseRepository
import com.g.pocketmal.domain.result.BrowseResult

class BrowseRepositoryImplementation(
    private val apiService: ApiService,
    private val converter: RankingEntityConverter,
    private val userSettings: UserSettingsStorage,
) : BrowseRepository {

    override suspend fun loadRankedTitles(
        titleType: TitleType,
        exploreType: ExploreType,
        offset: Int,
    ): BrowseResult {

        val dataTitleType = DataTitleType.from(titleType)
        val dataExploreType = DataExploreType.from(exploreType)
        val withNsfw = userSettings.getDisplayNsfw()
        val useEnglishTitle = userSettings.getShowEnglishTitles()

        val response = apiService.getRankingList(
            dataTitleType,
            dataExploreType,
            withNsfw,
            MalApi.BROWSE_PAGE_LIMIT,
            offset,
        )

        val body = response.body()

        if (response.isSuccessful && body != null) {
            val result = converter.transform(body, dataTitleType, useEnglishTitle)
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
