package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class Node(
    val id: Int,
    val title: String,
    @SerializedName("main_picture")
    val mainPicture: Picture?
)
