package com.g.pocketmal.domain.entity.converter

import com.g.pocketmal.data.api.response.RankingResponse
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.RankingEntity

class RankingEntityConverter {

    fun transform(response: RankingResponse, titleType: TitleType, useEnglishTitle: Boolean): List<RankingEntity> {

        return response.list.map { item ->

            val node = item.node
            val english = node.alternativeTitles?.english
            val titleLabel = if (useEnglishTitle && english != null && english.isNotEmpty())
                english else node.title

            val episodes = if (titleType == TitleType.ANIME)
                node.numEpisodes else node.numChapters

            RankingEntity(
                    node.id,
                    titleLabel,
                    node.mainPicture?.large,
                    node.mediaType,
                    episodes,
                    node.score,
                    node.menders,
                    node.startDate,
                    node.synopsis,
                    item.ranking.rank,
                    item.ranking.previousRank
            )
        }
    }
}
