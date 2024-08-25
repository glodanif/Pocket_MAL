package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class AlternativeTitles(
        val synonyms: List<String>?,
        @SerializedName("en")
        val english: String?,
        @SerializedName("ja")
        val japanese: String?
)