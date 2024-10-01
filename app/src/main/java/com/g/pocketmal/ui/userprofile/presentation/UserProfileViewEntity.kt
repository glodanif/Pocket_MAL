package com.g.pocketmal.ui.userprofile.presentation

import com.g.pocketmal.domain.ListCounts
import java.io.Serializable

data class UserProfileViewEntity(
    val id: Int,
    val name: String,
    val avatar: String?,
    val birthday: String,
    val location: String,
    val joinDate: String,
    val isSupporter: Boolean,
    val malLink: String,
    val animeSpentDays: String,
    val animeCounts: ListCounts,
    val animeMeanScore: String,
    val animeEpisodes: String,
    val animeRewatched: Int,
    val mangaSpentDays: String,
    val mangaCounts: ListCounts,
    val mangaMeanScore: String,
    val mangaChapters: String,
    val mangaVolumes: String,
    val mangaReread: Int,
    val isTraditionalGender: Boolean,
    val isFemale: Boolean,
    val isLocationAvailable: Boolean,
    val isBirthdayAvailable: Boolean,
    val genderLabel: String,
    val progresses: List<ProgressItem>,
) : Serializable
