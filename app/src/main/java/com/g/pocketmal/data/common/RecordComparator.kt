package com.g.pocketmal.data.common

import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.domain.SortingType

class RecordComparator(val sortingType: SortingType = SortingType.TITLE, val reverse: Boolean = false) : Comparator<DbListRecord> {

    private val typesOrder = listOf("tv", "ova", "movie", "special", "ona", "music", "manga", "one_shot", "doujin", "novel", "manhwa", "manhua")

    override fun compare(item1: DbListRecord, item2: DbListRecord): Int {

        when (sortingType) {
            SortingType.TITLE ->
                return (if (reverse) -1 else +1) * item1.seriesTitle.lowercase().compareTo(item2.seriesTitle.lowercase())
            SortingType.PROGRESS -> {
                val firstComparing = when {
                    item1.myEpisodes < item2.myEpisodes -> +1
                    item1.myEpisodes == item2.myEpisodes -> 0
                    else -> -1
                }
                return applyTitleSorting(firstComparing, item1, item2)
            }
            SortingType.SCORE -> {
                val firstComparing = if (item1.myScore < item2.myScore) +1 else if (item1.myScore == item2.myScore) 0 else -1
                return applyTitleSorting(firstComparing, item1, item2)
            }
            SortingType.TYPE -> {
                val firstComparing = typesOrder.indexOf(item1.seriesType).compareTo(typesOrder.indexOf(item2.seriesType))
                return applyTitleSorting(firstComparing, item1, item2)
            }
            SortingType.LAST_UPDATED -> {
                val firstComparing = if (item1.myLastUpdated < item2.myLastUpdated) +1 else if (item1.myLastUpdated == item2.myLastUpdated) 0 else -1
                return applyTitleSorting(firstComparing, item1, item2)
            }
        }
    }

    private fun applyTitleSorting(firstComparing: Int, item1: DbListRecord, item2: DbListRecord): Int {
        return if (firstComparing != 0) {
            (if (reverse) -1 else +1) * firstComparing
        } else {
            item1.seriesTitle.lowercase().compareTo(item2.seriesTitle.lowercase())
        }
    }
}
