package com.g.pocketmal.domain.comparator

import com.g.pocketmal.ui.viewmodel.RecordListViewModel

import java.util.Comparator

class RecordComparator(val sortingType: SortingType = SortingType.TITLE, val reverse: Boolean = false) : Comparator<RecordListViewModel> {

    private val typesOrder = listOf("tv", "ova", "movie", "special", "ona", "music", "manga", "one_shot", "doujin", "novel", "manhwa", "manhua")

    override fun compare(item1: RecordListViewModel, item2: RecordListViewModel): Int {

        when (sortingType) {
            SortingType.TITLE ->
                return (if (reverse) -1 else +1) * item1.seriesTitle.toLowerCase().compareTo(item2.seriesTitle.toLowerCase())
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
                val firstComparing = typesOrder.indexOf(item1.seriesMediaTypeRaw).compareTo(typesOrder.indexOf(item2.seriesMediaTypeRaw))
                return applyTitleSorting(firstComparing, item1, item2)
            }
            SortingType.LAST_UPDATED -> {
                val firstComparing = if (item1.lastUpdated < item2.lastUpdated) +1 else if (item1.lastUpdated == item2.lastUpdated) 0 else -1
                return applyTitleSorting(firstComparing, item1, item2)
            }
        }
    }

    private fun applyTitleSorting(firstComparing: Int, item1: RecordListViewModel, item2: RecordListViewModel): Int {
        return if (firstComparing != 0) {
            (if (reverse) -1 else +1) * firstComparing
        } else {
            item1.seriesTitle.toLowerCase().compareTo(item2.seriesTitle.toLowerCase())
        }
    }
}
