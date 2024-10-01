package com.g.pocketmal.ui.search.presentation

import android.content.Context
import com.g.pocketmal.R
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.entity.SearchResultTitle
import com.g.pocketmal.ui.common.inliststatus.InListStatusConverter
import com.g.pocketmal.util.list.DataInterpreter

class SearchResultConverter(
    private val context: Context,
    private val statusConverter: InListStatusConverter,
) {

    fun transform(item: SearchResultTitle, titleType: TitleType): SearchResultViewEntity {

        val score = item.score
        val scoreLabel = if (score != null && score > .01) item.score.toString() else "—"

        val mediaType = DataInterpreter.getMediaTypeLabelFromNetworkConst(item.mediaType)
        val episodes = if (titleType == TitleType.ANIME) item.episodes else item.chapters

        val episodesLabel = context.resources.getQuantityString(
            (if (titleType == TitleType.ANIME)
                R.plurals.shortEpisodes else R.plurals.shortChapters), episodes, episodes
        )

        val details = when {
            mediaType == "Unknown" && episodes == 0 -> ""
            mediaType == "Unknown" && episodes > 0 -> episodesLabel
            mediaType != "Unknown" && episodes == 0 -> mediaType
            else -> "$mediaType • $episodesLabel"
        }

        val synopsis = item.synopsis ?: context.getString(R.string.emptySynopsis)

        val inListStatus = statusConverter.transform(item.myListStatus, item.myScore, titleType)

        return SearchResultViewEntity(
            item.id,
            item.title,
            scoreLabel,
            item.picture,
            synopsis,
            details,
            inListStatus,
        )
    }

    fun transform(
        items: List<SearchResultTitle>,
        titleType: TitleType
    ): List<SearchResultViewEntity> {
        return items.map { transform(it, titleType) }
    }
}
