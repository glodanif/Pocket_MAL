package com.g.pocketmal.data.api.response

import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.RecommendationEntity
import com.google.gson.annotations.SerializedName

data class RecommendationsResponse(
    @SerializedName("recommendations") val recommendations: List<Recommendation>,
)
