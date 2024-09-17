package com.g.pocketmal.data.repository

import com.g.pocketmal.domain.entity.RankingEntity
import com.g.pocketmal.domain.entity.RecommendationEntity
import com.g.pocketmal.domain.entity.SearchEntity

sealed class BrowseResult {
    data class Result(val rankedItems: List<RankingEntity>) : BrowseResult()
    data object EmptyResult : BrowseResult()
    data class NetworkError(val message: String, val code: Int) : BrowseResult()
}
