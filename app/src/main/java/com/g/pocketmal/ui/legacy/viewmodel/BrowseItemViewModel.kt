package com.g.pocketmal.ui.legacy.viewmodel

data class BrowseItemViewModel(
        val id: Int,
        val title: String,
        val poster: String?,
        val mediaType: String,
        val startDate: String,
        val synopsis: String
)