package com.g.pocketmal.data

import com.g.pocketmal.domain.ThemeType

enum class DataThemeMode(val mode: String) {
    LIGHT("1"),
    DARK("2"),
    BLACK("3"),
    SYSTEM("4");

    fun toDomain(): ThemeType = when (this) {
        LIGHT -> ThemeType.LIGHT
        DARK -> ThemeType.DARK
        BLACK -> ThemeType.BLACK
        SYSTEM -> ThemeType.SYSTEM
    }

    companion object {
        fun from(findValue: String?) =
            DataThemeMode.entries.firstOrNull { it.mode == findValue } ?: SYSTEM

        fun from(mode: ThemeType?) = when (mode) {
            ThemeType.LIGHT -> LIGHT
            ThemeType.DARK -> DARK
            ThemeType.BLACK -> BLACK
            ThemeType.SYSTEM -> SYSTEM
            null -> SYSTEM
        }
    }
}
