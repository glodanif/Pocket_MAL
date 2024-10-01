package com.g.pocketmal.ui.seasonal.presentation

import com.g.pocketmal.domain.entity.SeasonalAnime
import com.g.pocketmal.util.AnimeSeason

class SeasonalSectionConverter(private val converter: SeasonalAnimeConverter) {

    private val sectionsOrder = listOf(
        "tv (new)",
        "tv (continuing)",
        "ona",
        "ova",
        "movie",
        "special",
        "music",
        "unknown"
    )

    fun transform(
        items: List<SeasonalAnime>,
        season: Season,
    ): List<SeasonalSectionViewEntity> {

        val sections = HashMap<String, com.g.pocketmal.domain.entity.SeasonSection>()

        items.forEach { item ->

            var mediaType = item.mediaType
            val startSeason = item.startSeason

            if (mediaType == "tv") {
                mediaType =
                    if (startSeason?.season == AnimeSeason.getSeasonText(season.partOfYear) && startSeason.year == season.year) {
                        "tv (new)"
                    } else {
                        "tv (continuing)"
                    }
            }

            val section = if (sections.containsKey(mediaType)) {
                sections[mediaType]
            } else {
                com.g.pocketmal.domain.entity.SeasonSection(mediaType)
            }

            if (section != null) {
                section.addItem(item)
                sections[mediaType] = section
            }
        }

        val sectionsList = ArrayList(sections.values)
        sectionsList.sortWith { item1, item2 ->
            val index1 = sectionsOrder.indexOf(item1.title).takeIf { it != -1 } ?: Int.MAX_VALUE
            val index2 = sectionsOrder.indexOf(item2.title).takeIf { it != -1 } ?: Int.MAX_VALUE
            index1.compareTo(index2)
        }

        return sectionsList.map { item ->
            SeasonalSectionViewEntity(
                item.title.uppercase(),
                converter.transform(item.items)
            )
        }
    }
}
