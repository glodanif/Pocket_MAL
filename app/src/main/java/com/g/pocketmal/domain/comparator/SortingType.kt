package com.g.pocketmal.domain.comparator

enum class SortingType(val type: Int) {
    TITLE(0),
    PROGRESS(1),
    SCORE(2),
    TYPE(3),
    LAST_UPDATED(4);

    companion object {
        fun from(findValue: Int) = values().first { it.type == findValue }
    }
}
