package com.g.pocketmal.data

import com.g.pocketmal.domain.DefaultList

enum class DataDefaultList(val list: String) {
    ANIME("1"),
    MANGA("2"),
    LATEST("3");

    fun toDomain(): DefaultList = when (this) {
        ANIME -> DefaultList.ANIME
        MANGA -> DefaultList.MANGA
        LATEST -> DefaultList.LATEST
    }

    companion object {
        fun from(findValue: String?) =
            DataDefaultList.entries.firstOrNull { it.list == findValue } ?: ANIME

        fun from(mode: DefaultList?) = when (mode) {
            DefaultList.ANIME -> ANIME
            DefaultList.MANGA -> MANGA
            DefaultList.LATEST -> LATEST
            null -> ANIME
        }
    }
}