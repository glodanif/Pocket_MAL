package com.g.pocketmal.ui.search.presentation

import android.content.Context
import com.g.pocketmal.R
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.SearchEntity
import com.g.pocketmal.util.list.DataInterpreter

class SearchResultConverter(private val context: Context) {

    fun transform(item: SearchEntity, titleType: TitleType): SearchResultViewEntity {

        val score = if (item.score != null && item.score > .01) item.score.toString() else "—"
        val scoreLabel = context.getString(R.string.scoreList, score)

        val mediaType = DataInterpreter.getMediaTypeLabelFromNetworkConst(item.mediaType)
        val mediaTypeLabel = context.getString(R.string.typeList, mediaType)

        val episodes = if (titleType == TitleType.ANIME) {
            if (item.episodes > 0) item.episodes.toString() else "—"
        } else {
            if (item.chapters > 0) item.chapters.toString() else "—"
        }
        val episodesLabel = context.getString(
                (if (titleType == TitleType.ANIME)
                    R.string.episodesSearchList else R.string.chaptersSearchList), episodes
        )

        val synopsis = item.synopsis ?: context.getString(R.string.emptySynopsis)

        return SearchResultViewEntity(
            item.id,
            item.title,
            scoreLabel,
            item.picture,
            synopsis,
            mediaTypeLabel,
            episodesLabel
        )
    }

    fun transform(items: List<SearchEntity>, titleType: TitleType): List<SearchResultViewEntity> {
        return items.map { transform(it, titleType) }
    }
}
