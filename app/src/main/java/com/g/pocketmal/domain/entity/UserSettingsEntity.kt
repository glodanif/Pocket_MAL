package com.g.pocketmal.domain.entity

import com.g.pocketmal.domain.DefaultList
import com.g.pocketmal.domain.FloatingSharingButtonOptions
import com.g.pocketmal.domain.ThemeMode

data class UserSettingsEntity(
    val showNsfwContentInList: Boolean,
    val showNsfwContentInExplore: Boolean,
    val themeMode: ThemeMode,
    val englishTitles: Boolean,
    val listAutoSync: Boolean,
    val saveSortingOrder: Boolean,
    val defaultList: DefaultList,
    val useExternalBrowser: Boolean,
    val showTagsInList: Boolean,
    val hidePostersInList: Boolean,
    val enableFloatingSharingButton: Boolean,
    val floatingSharingButtonOptions: FloatingSharingButtonOptions,
) {

    companion object {
        val default = UserSettingsEntity(
            showNsfwContentInExplore = false,
            showNsfwContentInList = true,
            englishTitles = false,
            listAutoSync = true,
            saveSortingOrder = false,
            themeMode = ThemeMode.SYSTEM,
            defaultList = DefaultList.ANIME,
            useExternalBrowser = false,
            showTagsInList = false,
            hidePostersInList = false,
            enableFloatingSharingButton = true,
            floatingSharingButtonOptions = FloatingSharingButtonOptions(
                onScoreChanges = true,
                onStatusChanges = true,
                onProgressChanges = true,
            ),
        )
    }
}
