package com.g.pocketmal.data.util

enum class TitleType(val type: Int) {
    ANIME(0),
    MANGA(1);

    companion object {
        fun from(findValue: Int) = values().first { it.type == findValue }
    }
}
