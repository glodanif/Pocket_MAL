package com.g.pocketmal.domain.entity.converter

import com.g.pocketmal.data.api.response.SearchResponse
import com.g.pocketmal.data.api.response.SearchedTitle
import com.g.pocketmal.domain.entity.SearchEntity

class SearchEntityConverter {

    fun transform(response: SearchResponse, useEnglishTitle: Boolean): List<SearchEntity> {

        return response.list.map { title ->

            val node = title.node
            val english = node.alternativeTitles?.english
            val titleLabel = if (useEnglishTitle && english != null && english.isNotEmpty())
                english else node.title

            SearchEntity(
                    node.id,
                    titleLabel,
                    node.picture?.large,
                    node.mediaType,
                    node.episodes,
                    node.chapters,
                    node.synopsis,
                    node.score,
                    node.nsfw
            )
        }
    }
}
