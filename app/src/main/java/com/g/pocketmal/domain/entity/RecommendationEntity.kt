package com.g.pocketmal.domain.entity

import com.g.pocketmal.domain.InListStatus

data class RecommendationEntity(
    val id: Int,
    val title: String,
    val picture: String?,
    val numRecommendations: Int,
    val score: Float?,
    val mediaType: String,
    val episodes: Int,
    val myListStatus: InListStatus,
    val myScore: Int?,
)
