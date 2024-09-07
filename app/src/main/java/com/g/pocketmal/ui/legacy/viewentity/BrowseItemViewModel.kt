package com.g.pocketmal.ui.legacy.viewentity

data class BrowseItemViewModel(
        val id: Int,
        val title: String,
        val poster: String?,
        val mediaType: String,
        val startDate: String,
        val synopsis: String
)