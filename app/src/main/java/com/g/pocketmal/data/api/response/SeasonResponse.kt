package com.g.pocketmal.data.api.response

import com.g.pocketmal.data.common.StartSeason
import com.google.gson.annotations.SerializedName

data class SeasonResponse(
        @SerializedName("data")
        val list: List<SeasonItem>,
        @SerializedName("paging")
        val pagingInfo: PagingInfo,
        val season: StartSeason
)
