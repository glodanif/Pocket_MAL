package com.g.pocketmal.data

import com.g.pocketmal.domain.ExploreType

enum class DataExploreType(val type: Int) {
    ALL(0),
    RELEASING(1),
    UPCOMING(2),
    TV(3),
    OVA(4),
    MOVIE(5),
    SPECIAL(6),
    BY_POPULARITY(7),
    FAVORITE(8);

    companion object {
        fun from(findValue: Int) = entries.first { it.type == findValue }
        fun from(type: ExploreType) = when (type) {
            ExploreType.TOP_RATED -> ALL
            ExploreType.MOST_POPULAR -> BY_POPULARITY
            ExploreType.RELEASING -> RELEASING
            ExploreType.UPCOMING -> UPCOMING
        }
    }
}