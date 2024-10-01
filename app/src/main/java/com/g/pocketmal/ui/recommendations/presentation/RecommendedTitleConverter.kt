package com.g.pocketmal.ui.recommendations.presentation

import android.content.Context
import com.g.pocketmal.R
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.entity.RecommendedTitle
import com.g.pocketmal.ui.common.inliststatus.InListStatusConverter
import com.g.pocketmal.util.list.DataInterpreter

class RecommendedTitleConverter(
    private val context: Context,
    private val statusConverter: InListStatusConverter,
) {

    fun transform(title: RecommendedTitle, titleType: TitleType): RecommendedTitleViewEntity {

        val numRecommendations = if (title.numRecommendations == 1) {
            context.getString(R.string.readRecommendation)
        } else {
            context.getString(R.string.readRecommendations, title.numRecommendations)
        }

        val score = title.score
        val scoreLabel = if (score != null && score > .01) score.toString() else "—"

        val mediaType = DataInterpreter.getMediaTypeLabelFromNetworkConst(title.mediaType)
        val episodes = title.episodes

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

        val inListStatus = statusConverter.transform(title.myListStatus, title.myScore, titleType)

        return RecommendedTitleViewEntity(
            title.id,
            title.title,
            title.picture,
            numRecommendations,
            details,
            scoreLabel,
            inListStatus,
        )
    }

    fun transform(
        titles: List<RecommendedTitle>,
        titleType: TitleType
    ): List<RecommendedTitleViewEntity> {
        val viewModels: MutableList<RecommendedTitleViewEntity> = ArrayList()
        for (title in titles) {
            viewModels.add(transform(title, titleType))
        }
        return viewModels
    }
}