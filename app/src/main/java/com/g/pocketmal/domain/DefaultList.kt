package com.g.pocketmal.domain

enum class DefaultList(val list: String) {
    ANIME("1"),
    MANGA("2"),
    LATEST("3");

    companion object {
        fun from(findValue: String?) =
            DefaultList.entries.firstOrNull { it.list == findValue } ?: ANIME
    }
}
