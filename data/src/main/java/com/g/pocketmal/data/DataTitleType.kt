package com.g.pocketmal.data

import com.g.pocketmal.domain.TitleType

enum class DataTitleType(val type: Int) {
    ANIME(0),
    MANGA(1);

    fun isAnime() = this == ANIME

    companion object {
        fun from(findValue: Int) = entries.first { it.type == findValue }
        fun from(titleType: TitleType) = if (titleType.isAnime()) ANIME else MANGA
    }
}
