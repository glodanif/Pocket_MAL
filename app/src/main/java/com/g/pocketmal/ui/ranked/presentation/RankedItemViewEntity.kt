package com.g.pocketmal.ui.ranked.presentation

data class RankedItemViewEntity(
    val id: Int,
    val title: String,
    val poster: String?,
    val members: String,
    val position: String,
    val type: String,
    val details: String
)
