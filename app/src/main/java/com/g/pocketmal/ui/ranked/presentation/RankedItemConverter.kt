package com.g.pocketmal.ui.ranked.presentation

import android.content.Context
import com.g.pocketmal.R
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.entity.RankingTitle
import com.g.pocketmal.util.list.DataInterpreter
import java.text.DecimalFormat

class RankedItemConverter(private val context: Context) {

    private val membersFormatter = DecimalFormat("#,###,###")

    fun transform(topItem: RankingTitle, titleType: TitleType): RankedItemViewEntity {

        val members = membersFormatter.format(topItem.menders).toString() + " members"

        val episodesLabel = context.getString(
            if (titleType == TitleType.ANIME) R.string.episodes else R.string.chapters
        ).lowercase()
        val mediaType = DataInterpreter.getMediaTypeLabelFromNetworkConst(topItem.mediaType)
        val episodesValue = if (topItem.numEpisodes == 0) "?" else topItem.numEpisodes.toString()
        val episodes = "$mediaType ($episodesValue $episodesLabel)"
        val details = context.getString(R.string.top__score, episodes, topItem.score ?: "?")

        return RankedItemViewEntity(
            topItem.id,
            topItem.title,
            topItem.mainPicture,
            members,
            "#" + topItem.rank,
            topItem.mediaType,
            details
        )
    }

    fun transform(topItems: List<RankingTitle>, titleType: TitleType): List<RankedItemViewEntity> {
        return topItems.map { transform(it, titleType) }
    }
}
