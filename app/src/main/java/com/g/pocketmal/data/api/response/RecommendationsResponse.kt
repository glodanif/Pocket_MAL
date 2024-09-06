package com.g.pocketmal.data.api.response

import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.RecommendationEntity
import com.google.gson.annotations.SerializedName

data class RecommendationsResponse(
    @SerializedName("recommendations") val recommendations: List<Recommendation>,
)

fun RecommendationsResponse.toDomainEntities(titleType: TitleType): List<RecommendationEntity> {
    return this.recommendations.map { title ->
        val episodes = if (titleType == TitleType.ANIME)
            title.node.numEpisodes else title.node.numChapters
        RecommendationEntity(
            title.node.id,
            title.node.title,
            title.node.mainPicture?.large,
            title.numRecommendations,
            title.node.mean,
            title.node.mediaType,
            episodes
        )
    }
}
