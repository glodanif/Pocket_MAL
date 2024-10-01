package com.g.pocketmal.data.converter

import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.data.api.response.RankingResponse
import com.g.pocketmal.domain.entity.RankingTitle

class RankingEntityConverter {

    fun transform(response: RankingResponse, titleType: DataTitleType, useEnglishTitle: Boolean): List<RankingTitle> {

        return response.list.map { item ->

            val node = item.node
            val english = node.alternativeTitles?.english
            val titleLabel = if (useEnglishTitle && !english.isNullOrEmpty())
                english else node.title

            val episodes = if (titleType == DataTitleType.ANIME)
                node.numEpisodes else node.numChapters

            RankingTitle(
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
