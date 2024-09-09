package com.g.pocketmal.data.util

enum class TitleType(val type: Int) {
    ANIME(0),
    MANGA(1);

    fun isAnime() = this == ANIME

    companion object {
        fun from(findValue: Int) = entries.first { it.type == findValue }
    }
}
