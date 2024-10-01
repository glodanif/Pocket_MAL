package com.g.pocketmal.data.converter

import com.g.pocketmal.data.api.response.SearchResponse
import com.g.pocketmal.domain.InListStatus
import com.g.pocketmal.domain.entity.SearchResultTitle

class SearchEntityConverter {

    fun transform(response: SearchResponse, useEnglishTitle: Boolean): List<SearchResultTitle> {

        return response.list.map { title ->

            val node = title.node
            val english = node.alternativeTitles?.english
            val titleLabel = if (useEnglishTitle && !english.isNullOrEmpty())
                english else node.title
            val inListStatus = InListStatus.parse(node.listStatus?.status)
            SearchResultTitle(
                node.id,
                titleLabel,
                node.picture?.large,
                node.mediaType,
                node.episodes,
                node.chapters,
                node.synopsis,
                node.score,
                node.nsfw,
                inListStatus,
                node.listStatus?.score,
            )
        }
    }
}
