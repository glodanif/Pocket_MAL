package com.g.pocketmal.data.repository

import com.g.pocketmal.domain.entity.RecommendationEntity
import com.g.pocketmal.domain.entity.SearchEntity

sealed class RecommendationsResult {
    data class Result(val recommendations: List<RecommendationEntity>) : RecommendationsResult()
    data object EmptyResult : RecommendationsResult()
    data class NetworkError(val message: String, val code: Int) : RecommendationsResult()
}
