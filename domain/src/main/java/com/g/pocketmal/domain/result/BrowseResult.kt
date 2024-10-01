package com.g.pocketmal.domain.result

import com.g.pocketmal.domain.entity.RankingTitle

sealed class BrowseResult {
    data class Result(val rankedItems: List<RankingTitle>) : BrowseResult()
    data object EmptyResult : BrowseResult()
    data class NetworkError(val message: String, val code: Int) : BrowseResult()
}
