package com.g.pocketmal.ui.viewmodel.converter

import android.content.Context
import com.g.pocketmal.R
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.RankingEntity
import com.g.pocketmal.util.list.DataInterpreter
import com.g.pocketmal.ui.viewmodel.RankedItemViewModel
import java.text.DecimalFormat

class RankedItemConverter(private val context: Context) {

    private val membersFormatter = DecimalFormat("#,###,###")

    fun transform(topItem: RankingEntity, titleType: TitleType): RankedItemViewModel {

        val members = membersFormatter.format(topItem.menders).toString() + " members"

        val episodesLabel = context.getString(
                if (titleType == TitleType.ANIME) R.string.episodes else R.string.chapters).toLowerCase()
        val mediaType = DataInterpreter.getMediaTypeLabelFromNetworkConst(topItem.mediaType)
        val episodesValue = if (topItem.numEpisodes == 0) "?" else topItem.numEpisodes.toString()
        val episodes = "$mediaType ($episodesValue $episodesLabel)"
        val details = context.getString(R.string.top__score, episodes, topItem.score ?: "?")

        return RankedItemViewModel(
                topItem.id,
                topItem.title,
                topItem.mainPicture,
                members,
                "#" + topItem.rank,
                topItem.mediaType,
                details
        )
    }

    fun transform(topItems: List<RankingEntity>, titleType: TitleType): List<RankedItemViewModel> {
        return topItems.map { transform(it, titleType) }
    }
}
