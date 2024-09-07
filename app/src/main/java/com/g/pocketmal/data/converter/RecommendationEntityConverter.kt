package com.g.pocketmal.data.converter

import com.g.pocketmal.data.api.response.RecommendationsResponse
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.RecommendationEntity

class RecommendationEntityConverter {

    fun transform(
        response: RecommendationsResponse,
        titleType: TitleType,
    ): List<RecommendationEntity> {

        return response.recommendations.map { title ->

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
}
