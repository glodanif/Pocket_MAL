package com.g.pocketmal.domain.entity

data class RecommendationEntity(
    val id: Int,
    val title: String,
    val picture: String?,
    val numRecommendations: Int,
    val score: Float?,
    val mediaType: String,
    val episodes: Int
)
