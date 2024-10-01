package com.g.pocketmal.domain.entity

import com.g.pocketmal.domain.InListStatus

data class SeasonalAnime(
    val id: Int,
    val title: String,
    val picture: Picture?,
    val mediaType: String,
    val episodes: Int,
    val source: String?,
    val genres: List<Genre>,
    val synopsis: String?,
    val broadcast: Broadcast?,
    val listUsers: Int,
    val score: Float?,
    val nsfw: String?,
    val startSeason: StartSeason?,
    val startDate: String?,
    val studios: List<Company>?,
    val myListStatus: InListStatus,
    val myScore: Int?,
)
