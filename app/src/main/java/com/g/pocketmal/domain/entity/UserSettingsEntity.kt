package com.g.pocketmal.domain.entity

import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.ThemeMode

data class UserSettingsEntity(
    val showNsfwContentInList: Boolean,
    val showNsfwContentInExplore: Boolean,
    val themeMode: ThemeMode,
    val englishTitles: Boolean,
    val listAutoSync: Boolean,
    val defaultList: TitleType,
) {

    companion object {
        val default = UserSettingsEntity(
            showNsfwContentInExplore = false,
            showNsfwContentInList = true,
            englishTitles = false,
            listAutoSync = true,
            themeMode = ThemeMode.SYSTEM,
            defaultList = TitleType.ANIME,
        )
    }
}
