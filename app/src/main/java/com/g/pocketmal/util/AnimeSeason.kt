package com.g.pocketmal.util

import com.g.pocketmal.domain.PartOfYear

//FIXME merge with Season class?
object AnimeSeason {

    fun parseSeason(monthOfYear: Int): PartOfYear {

        return when (monthOfYear) {
            in 0..2 -> PartOfYear.WINTER
            in 3..5 -> PartOfYear.SPRING
            in 6..8 -> PartOfYear.SUMMER
            else -> PartOfYear.FALL
        }
    }

    fun parseSeasonName(name: String): PartOfYear {
        return when {
            "winter".equals(name, ignoreCase = true) -> PartOfYear.WINTER
            "spring".equals(name, ignoreCase = true) -> PartOfYear.SPRING
            "summer".equals(name, ignoreCase = true) -> PartOfYear.SUMMER
            else -> PartOfYear.FALL
        }
    }

    fun getSeasonText(partOfYear: PartOfYear): String {
        return when (partOfYear) {
            PartOfYear.WINTER -> "winter"
            PartOfYear.SPRING -> "spring"
            PartOfYear.SUMMER -> "summer"
            else -> "fall"
        }
    }
}
