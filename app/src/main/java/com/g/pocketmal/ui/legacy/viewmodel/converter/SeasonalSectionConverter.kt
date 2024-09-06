package com.g.pocketmal.ui.legacy.viewmodel.converter

import com.g.pocketmal.data.util.Season
import com.g.pocketmal.domain.entity.SeasonSection
import com.g.pocketmal.domain.entity.SeasonEntity
import com.g.pocketmal.util.AnimeSeason
import java.util.ArrayList
import java.util.HashMap

class SeasonalSectionConverter(private val converter: SeasonalAnimeConverter) {

    private val sectionsOrder = listOf(
            "tv (new)", "tv (continuing)", "ona", "ova", "movie", "special", "music", "unknown"
    )

    fun transform(items: List<SeasonEntity>, season: Season): List<com.g.pocketmal.ui.legacy.viewmodel.SeasonalSectionViewModel> {

        val sections = HashMap<String, SeasonSection>()

        items.forEach { item ->

            var mediaType = item.mediaType
            val startSeason = item.startSeason

            if (mediaType == "tv") {
                mediaType = if (startSeason?.season == AnimeSeason.getSeasonText(season.partOfYear) && startSeason.year == season.year) {
                    "tv (new)"
                } else {
                    "tv (continuing)"
                }
            }

            val section = if (sections.containsKey(mediaType)) {
                sections[mediaType]
            } else {
                SeasonSection(mediaType)
            }

            if (section != null) {
                section.addItem(item)
                sections[mediaType] = section
            }
        }

        val sectionsList = ArrayList(sections.values)
        sectionsList.sortWith(Comparator { item1, item2 ->
            Integer.valueOf(sectionsOrder.indexOf(item1.title)).compareTo(sectionsOrder.indexOf(item2.title))
        })

        return sectionsList.map {item ->
            com.g.pocketmal.ui.legacy.viewmodel.SeasonalSectionViewModel(
                item.title,
                converter.transform(item.items)
            )
        }
    }
}
