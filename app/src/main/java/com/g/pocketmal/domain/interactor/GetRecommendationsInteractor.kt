package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.RecommendationEntity
import com.g.pocketmal.domain.entity.SearchEntity
import com.g.pocketmal.domain.entity.converter.RecommendationEntityConverter
import com.g.pocketmal.domain.entity.converter.SearchEntityConverter
import com.g.pocketmal.domain.exception.EmptyResponseException

class GetRecommendationsInteractor(
        private val apiService: ApiService,
        private val converter: RecommendationEntityConverter
) : BaseInteractor<GetRecommendationsInteractor.Params, List<RecommendationEntity>>() {

    override suspend fun execute(input: Params): List<RecommendationEntity> {

        val response = apiService.getRecommendations(input.recordId, input.titleType)
        val recommendationsResponse = response.body()

        if (response.isSuccessful && recommendationsResponse != null) {

            val result = converter.transform(recommendationsResponse, input.titleType)

            if (result.isEmpty()) {
                throw EmptyResponseException("No recommendations found for this title")
            }

            return result
        } else {
            throw NetworkException(response.code(), errorMessage = "Recommendations request was not successful")
        }
    }

    class Params(
            val recordId: Int,
            val titleType: TitleType
    )
}