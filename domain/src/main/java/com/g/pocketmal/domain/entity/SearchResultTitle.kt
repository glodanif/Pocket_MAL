package com.g.pocketmal.domain.entity

import com.g.pocketmal.domain.InListStatus

data class SearchResultTitle(
    val id: Int,
    val title: String,
    val picture: String?,
    val mediaType: String,
    val episodes: Int,
    val chapters: Int,
    val synopsis: String?,
    val score: Float?,
    val nsfw: String?,
    val myListStatus: InListStatus,
    val myScore: Int?,
)
