package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class RecommendationNode(
    val id: Int,
    val title: String,
    @SerializedName("main_picture")
    val mainPicture: Picture?,
    val mean: Float?,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("num_episodes")
    val numEpisodes: Int,
    @SerializedName("num_chapters")
    val numChapters: Int
)
