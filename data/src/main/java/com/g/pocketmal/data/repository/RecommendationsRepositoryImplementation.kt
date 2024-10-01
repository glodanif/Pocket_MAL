package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.converter.RecommendationEntityConverter
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.repository.RecommendationsRepository
import com.g.pocketmal.domain.result.RecommendationsResult

class RecommendationsRepositoryImplementation(
    private val apiService: ApiService,
    private val converter: RecommendationEntityConverter,
) : RecommendationsRepository {

    override suspend fun getRecommendations(id: Int, type: TitleType): RecommendationsResult {
        val response = apiService.getRecommendations(id, type)
        val body = response.body()

        if (response.isSuccessful && body != null) {
            val result = converter.transform(body, type)
            return if (body.recommendations.isNotEmpty()) {
                RecommendationsResult.Result(result)
            } else {
                RecommendationsResult.EmptyResult
            }
        } else {
            return RecommendationsResult.NetworkError(
                message = response.message(),
                code = response.code(),
            )
        }
    }
}