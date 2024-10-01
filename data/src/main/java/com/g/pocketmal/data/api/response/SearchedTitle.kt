package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

class SearchedTitle(
    val id: Int,
    val title: String,
    @SerializedName("main_picture")
    val picture: Picture?,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("num_episodes")
    val episodes: Int,
    @SerializedName("num_chapters")
    val chapters: Int,
    @SerializedName("num_volumes")
    val volumes: Int,
    val synopsis: String?,
    @SerializedName("mean")
    val score: Float?,
    val nsfw: String?,
    @SerializedName("alternative_titles")
    val alternativeTitles: AlternativeTitles?,
    @SerializedName("my_list_status")
    val listStatus: ListStatus?,
)
