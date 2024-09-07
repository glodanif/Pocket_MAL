package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.MalApi
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.RankingEntity
import com.g.pocketmal.data.converter.RankingEntityConverter
import com.g.pocketmal.domain.exception.EmptyResponseException

class GetTopInteractor(
        private val apiService: ApiService,
        private val settings: MainSettings,
        private val converter: RankingEntityConverter
) : BaseInteractor<GetTopInteractor.Params, List<RankingEntity>>() {

    override suspend fun execute(input: Params): List<RankingEntity> {

        val includeNsfw = settings.displayNsfw()

        val response = apiService
                .getRankingList(input.titleType, input.rankingType, includeNsfw, MalApi.BROWSE_PAGE_LIMIT, input.offset)
        val rankingResponse = response.body()

        if (response.isSuccessful && rankingResponse != null) {

            val useEnglishTitles = settings.showEnglishTitles()
            val result = converter.transform(rankingResponse, input.titleType, useEnglishTitles)

            if (result.isEmpty()) {
                throw EmptyResponseException("Empty ranking response")
            }

            return result
        } else {
            throw NetworkException(response.code(), errorMessage = "Ranking request was not successful")
        }
    }

    class Params(
            val titleType: TitleType,
            val rankingType: RankingType,
            val offset: Int
    )
}
