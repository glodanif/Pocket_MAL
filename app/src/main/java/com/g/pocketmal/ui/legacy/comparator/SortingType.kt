package com.g.pocketmal.ui.legacy.comparator

enum class SortingType(val type: Int) {
    TITLE(0),
    PROGRESS(1),
    SCORE(2),
    TYPE(3),
    LAST_UPDATED(4);

    companion object {
        fun from(findValue: Int) = entries.first { it.type == findValue }
    }
}
