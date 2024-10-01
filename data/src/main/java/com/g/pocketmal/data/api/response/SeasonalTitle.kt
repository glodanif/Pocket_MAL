package com.g.pocketmal.data.api.response

import com.g.pocketmal.data.common.Broadcast
import com.g.pocketmal.data.common.Company
import com.g.pocketmal.data.common.Genre
import com.g.pocketmal.data.common.StartSeason
import com.google.gson.annotations.SerializedName

class SeasonalTitle(
    val id: Int,
    val title: String,
    @SerializedName("main_picture")
    val picture: Picture?,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("num_episodes")
    val episodes: Int,
    val source: String?,
    val genres: List<Genre>?,
    val synopsis: String?,
    val broadcast: Broadcast?,
    @SerializedName("num_list_users")
    val listUsers: Int,
    @SerializedName("mean")
    val score: Float?,
    val nsfw: String?,
    @SerializedName("start_season")
    val startSeason: StartSeason?,
    @SerializedName("start_date")
    val startDate: String?,
    val studios: List<Company>?,
    @SerializedName("alternative_titles")
    val alternativeTitles: AlternativeTitles?,
    @SerializedName("my_list_status")
    val listStatus: ListStatus?,
)
