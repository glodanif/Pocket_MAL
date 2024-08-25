package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class RankingNode(
    val id: Int,
    val title: String,
    @SerializedName("main_picture")
    val mainPicture: Picture?,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("num_episodes")
    val numEpisodes: Int,
    @SerializedName("num_chapters")
    val numChapters: Int,
    @SerializedName("mean")
    val score: Float?,
    @SerializedName("num_list_users")
    val menders: Int,
    @SerializedName("start_date")
    val startDate: String?,
    val synopsis: String?,
    @SerializedName("alternative_titles")
    val alternativeTitles: AlternativeTitles?
)
