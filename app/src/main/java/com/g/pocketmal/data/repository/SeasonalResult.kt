package com.g.pocketmal.data.repository

import com.g.pocketmal.domain.entity.SeasonEntity

sealed class SeasonalResult {
    data class Result(val seasonalAnime: List<SeasonEntity>) : SeasonalResult()
    data object EmptyResult : SeasonalResult()
    data class NetworkError(val message: String, val code: Int) : SeasonalResult()
}
