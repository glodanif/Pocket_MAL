package com.g.pocketmal.domain.repository

import com.g.pocketmal.domain.PartOfYear
import com.g.pocketmal.domain.result.SeasonalResult

interface SeasonalRepository {
    suspend fun getSeasonalAnime(year: Int, season: PartOfYear): SeasonalResult
}
