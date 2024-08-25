package com.g.pocketmal.data.common

import com.google.gson.annotations.SerializedName

class Broadcast(
        @SerializedName("day_of_the_week")
        val dayOfTheWeek: String,
        @SerializedName("start_time")
        val startTime: String?
)
