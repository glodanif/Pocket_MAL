package com.g.pocketmal.domain.entity

import com.g.pocketmal.domain.ListCounts

data class UserProfile(
    val id: Int,
    val name: String,
    val avatar: String?,
    val gender: String?,
    val birthday: String?,
    val location: String?,
    val joinDate: Long,
    val isSupporter: Boolean,
    val animeSpentDays: Float,
    val animeCounts: ListCounts,
    val animeMeanScore: Float,
    val animeEpisodes: Int,
    val animeRewatched: Int,
    val mangaSpentDays: Float,
    val mangaCounts: ListCounts,
    val mangaMeanScore: Float,
    val mangaChapters: Int,
    val mangaVolumes: Int,
    val mangaReread: Int
)
