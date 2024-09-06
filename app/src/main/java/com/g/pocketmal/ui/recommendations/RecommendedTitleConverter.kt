package com.g.pocketmal.ui.recommendations

import android.content.Context
import android.text.TextUtils
import com.g.pocketmal.R
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.RecommendationEntity
import com.g.pocketmal.util.list.DataInterpreter

class RecommendedTitleConverter(private val context: Context) {

    fun transform(title: RecommendationEntity, titleType: TitleType): RecommendedTitleViewEntity {

        val numRecommendations = if (title.numRecommendations == 1) {
            context.getString(R.string.readRecommendation)
        } else {
            context.getString(R.string.readRecommendations, title.numRecommendations)
        }

        val details = getDetailsLine(title, titleType)

        return RecommendedTitleViewEntity(
                title.id,
                title.title,
                title.picture,
                numRecommendations,
                details
        )
    }

    fun transform(titles: List<RecommendationEntity>, titleType: TitleType): List<RecommendedTitleViewEntity> {
        val viewModels: MutableList<RecommendedTitleViewEntity> = ArrayList()
        for (title in titles) {
            viewModels.add(transform(title, titleType))
        }
        return viewModels
    }

    private fun getDetailsLine(details: RecommendationEntity, titleType: TitleType): String {

        val stats = ArrayList<String>()
        if (details.score != null) {
            stats.add(context.getString(R.string.score) + ": " + details.score)
        }

        val mediaType = DataInterpreter.getMediaTypeLabelFromNetworkConst(details.mediaType)
        stats.add(mediaType)

        val episodesTypeLabel = context.getString(
                if (titleType == TitleType.ANIME) R.string.episodes else R.string.chapters)
        val episodesPlural = episodesTypeLabel.lowercase()
                .substring(0, episodesTypeLabel.length - if (details.episodes == 1) 1 else 0)
        val episodesLabel = (if (details.episodes == 0)
            "?" else details.episodes.toString()) + " " + episodesPlural

        stats.add(episodesLabel)

        return if (stats.isNotEmpty()) TextUtils.join(" • ", stats) else "?"
    }
}