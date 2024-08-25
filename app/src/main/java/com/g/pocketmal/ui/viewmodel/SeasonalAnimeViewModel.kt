package com.g.pocketmal.ui.viewmodel

import android.text.Spannable

class SeasonalAnimeViewModel(
        val id: Int,
        val title: String,
        val poster: String?,
        val info: String,
        val genres: String,
        val synopsis: String,
        val airing: String,
        val members: String,
        val score: Spannable
)
