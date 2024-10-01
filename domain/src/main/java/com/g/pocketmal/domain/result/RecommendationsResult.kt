package com.g.pocketmal.domain.result

import com.g.pocketmal.domain.entity.RecommendedTitle

sealed class RecommendationsResult {
    data class Result(val recommendations: List<RecommendedTitle>) : RecommendationsResult()
    data object EmptyResult : RecommendationsResult()
    data class NetworkError(val message: String, val code: Int) : RecommendationsResult()
}
