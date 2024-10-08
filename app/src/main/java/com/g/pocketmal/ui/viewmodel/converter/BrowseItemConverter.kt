package com.g.pocketmal.ui.viewmodel.converter

import android.content.Context
import com.g.pocketmal.R
import com.g.pocketmal.domain.entity.RankingEntity
import com.g.pocketmal.reformatToViewableDate
import com.g.pocketmal.ui.viewmodel.BrowseItemViewModel
import com.g.pocketmal.util.list.DataInterpreter
import java.text.SimpleDateFormat
import java.util.*

class BrowseItemConverter(private val context: Context) {

    fun transform(topItem: RankingEntity): BrowseItemViewModel {

        val mediaType = DataInterpreter.getMediaTypeLabelFromNetworkConst(topItem.mediaType)
        val mediaTypeLabel = context.getString(R.string.filter__type, mediaType)
        val synopsis = topItem.synopsis ?: context.getString(R.string.emptySynopsis)

        val dateLabel = context
                .getString(R.string.filter__start_date, topItem.startDate.reformatToViewableDate())

        return BrowseItemViewModel(
                topItem.id,
                topItem.title,
                topItem.mainPicture,
                mediaTypeLabel,
                dateLabel,
                synopsis
        )
    }

    fun transform(topItems: List<RankingEntity>): List<BrowseItemViewModel> {
        return topItems.map { transform(it) }
    }
}
