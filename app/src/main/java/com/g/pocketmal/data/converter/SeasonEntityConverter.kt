package com.g.pocketmal.data.converter

import com.g.pocketmal.data.api.response.SeasonResponse
import com.g.pocketmal.data.common.Genre
import com.g.pocketmal.domain.entity.SeasonEntity

class SeasonEntityConverter {

    fun transform(response: SeasonResponse, useEnglishTitle: Boolean): List<SeasonEntity> {

        val items = ArrayList<SeasonEntity>()

        response.list.forEach {item ->
            item.node?.let { node ->

                val english = node.alternativeTitles?.english
                val titleLabel = if (useEnglishTitle && english != null && english.isNotEmpty())
                    english else node.title

                items.add(
                        SeasonEntity(
                                id = node.id,
                                title = titleLabel,
                                picture = node.picture,
                                mediaType = node.mediaType,
                                episodes = node.episodes,
                                source = node.source,
                                genres = node.genres ?: listOf(Genre(-1, "—")),
                                synopsis = node.synopsis,
                                broadcast = node.broadcast,
                                listUsers = node.listUsers,
                                score = node.score,
                                nsfw = node.nsfw,
                                startSeason = node.startSeason,
                                startDate = node.startDate,
                                studios = node.studios
                        )
                )
            }
        }

        return items
    }
}
