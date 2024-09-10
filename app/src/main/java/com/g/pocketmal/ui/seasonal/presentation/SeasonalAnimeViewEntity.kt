package com.g.pocketmal.ui.seasonal.presentation

import com.g.pocketmal.ui.common.inliststatus.InListStatusViewEntity

class SeasonalAnimeViewEntity(
    val id: Int,
    val title: String,
    val poster: String?,
    val airing: String?,
    val source: String?,
    val studio: String?,
    val genres: String,
    val synopsis: String,
    val members: String,
    val score: String,
    val inListStatus: InListStatusViewEntity,
)
