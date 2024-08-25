package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class RelatedTitleEdge(
    val node: Node,
    @SerializedName("relation_type")
    val relationType: String,
    @SerializedName("relation_type_formatted")
    val relationTypeFormatted: String
)