package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

class RankingResponse(
    @SerializedName("data")
    val list: List<RankingItem>,
    @SerializedName("paging")
    val pagingInfo: PagingInfo
)
