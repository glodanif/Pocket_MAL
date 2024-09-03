package com.g.pocketmal.ui

import com.g.pocketmal.data.keyvalue.MainSettings

enum class ThemeMode(@MainSettings.Companion.Theme private val theme: String) {
    LIGHT("1"),
    DARK("2"),
    BLACK("3");

    companion object {
        fun fromTheme(theme: String?): ThemeMode {
            return entries.find { it.theme == theme } ?: LIGHT
        }
    }
}