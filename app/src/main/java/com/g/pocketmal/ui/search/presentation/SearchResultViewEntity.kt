package com.g.pocketmal.ui.search.presentation

import com.g.pocketmal.ui.common.InListStatusViewEntity

data class SearchResultViewEntity(
    val id: Int,
    val title: String,
    val score: String,
    val poster: String?,
    val synopsis: String,
    val details: String,
    val inListStatus: InListStatusViewEntity,
)
