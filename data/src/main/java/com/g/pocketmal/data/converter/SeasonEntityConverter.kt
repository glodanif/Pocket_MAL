package com.g.pocketmal.data.converter

import com.g.pocketmal.data.api.response.SeasonResponse
import com.g.pocketmal.data.common.Genre
import com.g.pocketmal.domain.InListStatus
import com.g.pocketmal.domain.entity.SeasonalAnime

class SeasonEntityConverter {

    fun transform(response: SeasonResponse, useEnglishTitle: Boolean): List<SeasonalAnime> {

        val items = ArrayList<SeasonalAnime>()

        response.list.forEach { item ->
            item.node?.let { node ->

                val english = node.alternativeTitles?.english
                val titleLabel = if (useEnglishTitle && !english.isNullOrEmpty())
                    english else node.title

                items.add(
                    SeasonalAnime(
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
                        studios = node.studios,
                        myListStatus = InListStatus.parse(node.listStatus?.status),
                        myScore = node.listStatus?.score,
                    )
                )
            }
        }

        return items
    }
}
