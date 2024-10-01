package com.g.pocketmal.data.converter

import com.g.pocketmal.data.api.response.RecommendationsResponse
import com.g.pocketmal.domain.InListStatus
import com.g.pocketmal.domain.entity.RecommendedTitle

class RecommendationEntityConverter {

    fun transform(
        response: RecommendationsResponse,
        titleType: TitleType,
    ): List<RecommendedTitle> {

        return response.recommendations.map { title ->

            val episodes = if (titleType == TitleType.ANIME)
                title.node.numEpisodes else title.node.numChapters
            val inListStatus = InListStatus.parse(title.node.listStatus?.status)
            RecommendedTitle(
                title.node.id,
                title.node.title,
                title.node.mainPicture?.large,
                title.numRecommendations,
                title.node.mean,
                title.node.mediaType,
                episodes,
                inListStatus,
                title.node.listStatus?.score,
            )
        }
    }
}
