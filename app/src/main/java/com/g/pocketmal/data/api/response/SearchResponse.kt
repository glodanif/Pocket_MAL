package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("data")
    val list: List<SearchedItem>,
    @SerializedName("paging")
    val pagingInfo: PagingInfo,
)