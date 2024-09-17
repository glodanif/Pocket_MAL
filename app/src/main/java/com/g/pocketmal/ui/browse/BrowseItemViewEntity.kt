package com.g.pocketmal.ui.browse

data class BrowseItemViewEntity(
        val id: Int,
        val title: String,
        val poster: String?,
        val mediaType: String,
        val startDate: String,
        val synopsis: String
)