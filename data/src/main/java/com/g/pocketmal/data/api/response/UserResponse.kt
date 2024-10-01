package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
        val id: Int,
        val name: String,
        val picture: String?,
        val location: String?,
        val gender: String?,
        val birthday: String?,
        @SerializedName("joined_at")
        val joinedAt: String,
        @SerializedName("time_zone")
        val timeZone: String?,
        @SerializedName("is_supporter")
        val isSupporter: Boolean?,
        @SerializedName("anime_statistics")
        val animeStatistics: UserStatistics?
)
