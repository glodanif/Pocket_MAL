package com.g.pocketmal.domain.repository

import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.result.RecommendationsResult

interface RecommendationsRepository {
    suspend fun getRecommendations(id: Int, type: TitleType): RecommendationsResult
}
