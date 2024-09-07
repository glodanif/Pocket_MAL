package com.g.pocketmal.ui.search.presentation

import android.content.Context
import com.g.pocketmal.R
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.SearchEntity
import com.g.pocketmal.util.list.DataInterpreter

class SearchResultConverter(private val context: Context) {

    fun transform(item: SearchEntity, titleType: TitleType): SearchResultViewEntity {

        val score = if (item.score != null && item.score > .01) item.score.toString() else "—"

        val mediaType = DataInterpreter.getMediaTypeLabelFromNetworkConst(item.mediaType)
        val episodes = if (titleType == TitleType.ANIME) item.episodes else item.chapters

        val episodesLabel = context.resources.getQuantityString(
            (if (titleType == TitleType.ANIME)
                R.plurals.shortEpisodes else R.plurals.shortChapters), episodes, episodes)

        val details = when {
            mediaType == "Unknown" && episodes == 0 -> ""
            mediaType == "Unknown" && episodes > 0 -> episodesLabel
            mediaType != "Unknown" && episodes == 0 -> mediaType
            else -> "$mediaType • $episodesLabel"
        }

        val synopsis = item.synopsis ?: context.getString(R.string.emptySynopsis)

        return SearchResultViewEntity(
            item.id,
            item.title,
            score,
            item.picture,
            synopsis,
            details,
        )
    }

    fun transform(items: List<SearchEntity>, titleType: TitleType): List<SearchResultViewEntity> {
        return items.map { transform(it, titleType) }
    }
}
