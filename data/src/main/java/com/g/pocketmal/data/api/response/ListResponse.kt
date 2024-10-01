package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class ListResponse(
        @SerializedName("data")
        val list: List<ListItem>,
        @SerializedName("paging")
        val pagingInfo: PagingInfo
)
