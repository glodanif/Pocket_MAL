package com.g.pocketmal.ui.viewmodel

data class BrowseItemViewModel(
        val id: Int,
        val title: String,
        val poster: String?,
        val mediaType: String,
        val startDate: String,
        val synopsis: String
)