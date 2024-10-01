package com.g.pocketmal.domain.result

import com.g.pocketmal.domain.entity.SeasonalAnime

sealed class SeasonalResult {
    data class Result(val seasonalAnime: List<SeasonalAnime>) : SeasonalResult()
    data object EmptyResult : SeasonalResult()
    data class NetworkError(val message: String, val code: Int) : SeasonalResult()
}
