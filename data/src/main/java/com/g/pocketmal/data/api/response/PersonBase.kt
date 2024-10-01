package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class PersonBase(
        val id: Int,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("last_name")
        val lastName: String
)