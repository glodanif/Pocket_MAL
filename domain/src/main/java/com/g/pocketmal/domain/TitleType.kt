package com.g.pocketmal.domain

enum class TitleType {
    ANIME, MANGA;

    fun isAnime() = this == ANIME
}
