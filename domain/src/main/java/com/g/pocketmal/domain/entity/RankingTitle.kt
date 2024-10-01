package com.g.pocketmal.domain.entity

data class RankingTitle(
    val id: Int,
    val title: String,
    val mainPicture: String?,
    val mediaType: String,
    val numEpisodes: Int,
    val score: Float?,
    val menders: Int,
    val startDate: String?,
    val synopsis: String?,
    val rank: Int,
    val previousRank: Int?
)
