package com.g.pocketmal.data.api.response

import com.g.pocketmal.data.common.Broadcast
import com.google.gson.annotations.SerializedName

data class RecordNode(
    val id: Int,
    val title: String,
    @SerializedName("main_picture")
    val mainPicture: Picture?,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("num_episodes")
    val numEpisodes: Int?,
    @SerializedName("num_chapters")
    val numChapters: Int?,
    @SerializedName("num_volumes")
    val numVolumes: Int?,
    val status: String,
    val broadcast: Broadcast?,
    @SerializedName("my_list_status")
    val listStatus: ListStatus?,
    @SerializedName("alternative_titles")
    val alternativeTitles: AlternativeTitles?
)