package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class Recommendation(
        val node: RecommendationNode,
        @SerializedName("num_recommendations")
        val numRecommendations: Int
)