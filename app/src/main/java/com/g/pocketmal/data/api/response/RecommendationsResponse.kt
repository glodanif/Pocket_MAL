package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class RecommendationsResponse(@SerializedName("recommendations") val recommendations: List<Recommendation>)