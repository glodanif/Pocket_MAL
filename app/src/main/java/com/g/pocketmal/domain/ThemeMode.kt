package com.g.pocketmal.domain

enum class ThemeMode(val mode: String) {
    LIGHT("1"),
    DARK("2"),
    BLACK("3"),
    SYSTEM("4");

    companion object {
        fun from(findValue: String?) =
            ThemeMode.entries.firstOrNull { it.mode == findValue } ?: SYSTEM
    }
}
