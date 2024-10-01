package com.g.pocketmal.data

import com.g.pocketmal.domain.SortingType

enum class DataSortingType(val type: Int) {
    TITLE(0),
    PROGRESS(1),
    SCORE(2),
    TYPE(3),
    LAST_UPDATED(4);

    companion object {
        fun from(findValue: Int) = entries.first { it.type == findValue }
        fun to(type: DataSortingType) = when (type) {
            TITLE -> SortingType.TITLE
            PROGRESS -> SortingType.PROGRESS
            SCORE -> SortingType.SCORE
            TYPE -> SortingType.TYPE
            LAST_UPDATED -> SortingType.LAST_UPDATED
        }
    }
}
