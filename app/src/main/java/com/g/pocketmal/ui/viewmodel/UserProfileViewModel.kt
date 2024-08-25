package com.g.pocketmal.ui.viewmodel

import androidx.annotation.DrawableRes
import com.g.pocketmal.data.common.ListCounts
import java.io.Serializable

data class UserProfileViewModel(
        val id: Int,
        val name: String,
        val avatar: String?,
        @DrawableRes
        val genderDrawable: Int,
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
        val isNormalGender: Boolean,
        val isLocationAvailable: Boolean,
        val isBirthdayAvailable: Boolean
) : Serializable