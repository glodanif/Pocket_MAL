package com.g.pocketmal.data.keyvalue

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.g.pocketmal.data.DataDefaultList
import com.g.pocketmal.data.DataThemeMode
import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.domain.FloatingSharingButtonOptions
import com.g.pocketmal.domain.entity.UserSettings

class UserSettingsStorage(context: Context) {

    //FIXME: DI
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun getUserSettings(): UserSettings {
        return UserSettings(
            showNsfwContentInList = getDisplayNsfwInList(),
            showNsfwContentInExplore = getDisplayNsfw(),
            themeMode = getThemeMode().toDomain(),
            englishTitles = getShowEnglishTitles(),
            listAutoSync = isAutoSyncEnabled(),
            saveSortingOrder = isSaveSortingOrderEnabled(),
            defaultList = getDefaultList().toDomain(),
            useExternalBrowser = useExternalBrowser(),
            showTagsInList = showTagsInList(),
            hidePostersInList = hidePostersInList(),
            enableFloatingSharingButton = getFloatingSharingButtonEnabled(),
            floatingSharingButtonOptions = getFloatingSharingButtonOptions(),
        )
    }

    //FIXME: MOVE TO VIEWMODEL/REPOSITORY
    /*fun shouldShowFloatingSharingButton(
        actionType: Action,
        title: RecordViewModel
    ): Boolean {

        if (actionType == Action.ACTION_SHARE) {
            return true
        }

        if (!getFloatingSharingButtonEnabled()) {
            return false
        }

        val options = getFloatingSharingButtonOptions()

        return when (actionType) {
            Action.ACTION_SCORE -> options.onScoreChanges && title.myScore > 0
            Action.ACTION_STATUS, Action.ACTION_REWATCHED -> options.onStatusChanges
            Action.ACTION_EPISODES,
            Action.ACTION_CHAPTERS,
            Action.ACTION_VOLUMES -> options.onScoreChanges

            else -> false
        }
    }*/

    fun getFloatingSharingButtonOptions(): FloatingSharingButtonOptions {
        val defaultValue = UserSettings.default.floatingSharingButtonOptions.toOptionString()
        val options = preferences.getString(ACTION_POPUP_OPTIONS_KEY, defaultValue) ?: defaultValue
        return options.fromOptionString()
    }

    fun setFloatingSharingButtonOptions(options: FloatingSharingButtonOptions) {
        preferences.edit {
            putString(ACTION_POPUP_OPTIONS_KEY, options.toOptionString())
        }
    }

    fun setFloatingSharingButtonEnabled(enabled: Boolean) {
        preferences.edit {
            putBoolean(SIMPLE_VIEW_KEY, enabled)
        }
    }

    fun getFloatingSharingButtonEnabled(): Boolean =
        preferences.getBoolean(
            ACTION_POPUP_KEY,
            UserSettings.default.enableFloatingSharingButton
        )

    fun setHidePostersInListEnabled(enabled: Boolean) {
        preferences.edit {
            putBoolean(SIMPLE_VIEW_KEY, enabled)
        }
    }

    fun hidePostersInList(): Boolean =
        preferences.getBoolean(SIMPLE_VIEW_KEY, UserSettings.default.hidePostersInList)

    fun setShowTagsInListEnabled(enabled: Boolean) {
        preferences.edit {
            putBoolean(TAGS_IN_LIST_KEY, enabled)
        }
    }

    fun showTagsInList(): Boolean =
        preferences.getBoolean(TAGS_IN_LIST_KEY, UserSettings.default.showTagsInList)

    fun setUseExternalBrowserEnabled(enabled: Boolean) {
        preferences.edit {
            putBoolean(EXTERNAL_BROWSER_KEY, enabled)
        }
    }

    fun useExternalBrowser(): Boolean {
        return preferences
            .getBoolean(EXTERNAL_BROWSER_KEY, UserSettings.default.useExternalBrowser)
    }

    fun isSaveSortingOrderEnabled(): Boolean =
        preferences.getBoolean(SAVE_SORTING_STATE_KEY, UserSettings.default.saveSortingOrder)

    fun setSaveSortingOrderEnabled(enabled: Boolean) =
        preferences.edit {
            putBoolean(SAVE_SORTING_STATE_KEY, enabled)
        }

    fun getDefaultList(): DataDefaultList {
        val defaultListOption = preferences
            .getString(
                DEFAULT_LIST_KEY,
                DataDefaultList.from(UserSettings.default.defaultList).list
            )
        return DataDefaultList.from(defaultListOption)
    }

    fun setDefaultList(list: DataDefaultList) {
        preferences.edit {
            putString(DEFAULT_LIST_KEY, list.list)
        }
    }

    fun getLastOpenedList(): DataTitleType {
        val type = preferences
            .getInt(LAST_OPENED_LIST_KEY, DataTitleType.ANIME.type)
        return DataTitleType.from(type)
    }

    fun setLastOpenedList(type: DataTitleType) {
        preferences.edit {
            putInt(LAST_OPENED_LIST_KEY, type.type)
        }
    }

    fun isAutoSyncEnabled(): Boolean =
        preferences.getBoolean(AUTO_SYNC_KEY, UserSettings.default.listAutoSync)

    fun setAutoSyncEnabled(enabled: Boolean) =
        preferences.edit {
            putBoolean(AUTO_SYNC_KEY, enabled)
        }

    fun getShowEnglishTitles(): Boolean =
        preferences.getBoolean(ENGLISH_TITLES, UserSettings.default.englishTitles)

    fun setShowEnglishTitles(enabled: Boolean) {
        preferences.edit {
            putBoolean(ENGLISH_TITLES, enabled)
        }
    }

    fun getThemeMode(): DataThemeMode {
        val defaultTheme = UserSettings.default.themeMode
        val theme = preferences.getString(THEME_KEY, DataThemeMode.from(defaultTheme).mode)
        return DataThemeMode.from(theme)
    }

    fun setThemeMode(themeMode: DataThemeMode) {
        preferences.edit {
            putString(THEME_KEY, themeMode.mode)
        }
    }

    fun getDisplayNsfw(): Boolean =
        preferences.getBoolean(
            HENTAI_FILTER_BROWSE_KEY,
            UserSettings.default.showNsfwContentInExplore,
        )

    fun setDisplayNsfw(enabled: Boolean) {
        preferences.edit {
            putBoolean(HENTAI_FILTER_BROWSE_KEY, enabled)
        }
    }

    fun getDisplayNsfwInList(): Boolean =
        preferences.getBoolean(
            HENTAI_FILTER_LIST_KEY,
            UserSettings.default.showNsfwContentInList,
        )

    fun setDisplayNsfwInList(enabled: Boolean) {
        preferences.edit {
            putBoolean(HENTAI_FILTER_LIST_KEY, enabled)
        }
    }

    fun migrate() {
        if (preferences.getBoolean("darkTheme", false)) {
            preferences.edit {
                putString("theme", DataThemeMode.DARK.mode)
                remove("darkTheme")
            }
        }
    }

    companion object {

        const val DEFAULT_LIST_KEY = "defaultList"
        const val LAST_OPENED_LIST_KEY = "lastOpenedList"
        const val AUTO_SYNC_KEY = "autoSynch"
        const val SIMPLE_VIEW_KEY = "simpleView"
        const val THEME_KEY = "theme"
        const val EXTERNAL_BROWSER_KEY = "externalBrowser"
        const val SAVE_SORTING_STATE_KEY = "saveSortingState"
        const val TAGS_IN_LIST_KEY = "tagsInListVisibility"
        const val ENGLISH_TITLES = "englishTitles"
        const val ACTION_POPUP_KEY = "actionPopup"
        const val ACTION_POPUP_OPTIONS_KEY = "actionPopupOptions"
        const val HENTAI_FILTER_BROWSE_KEY = "hentaiFilter"
        const val HENTAI_FILTER_LIST_KEY = "hentaiFilterList"
    }
}

private fun FloatingSharingButtonOptions.toOptionString(): String {
    val options = mutableListOf<String>()
    if (onScoreChanges) options.add("1")
    if (onStatusChanges) options.add("2")
    if (onProgressChanges) options.add("3")
    return options.joinToString("|")
}

private fun String.fromOptionString(): FloatingSharingButtonOptions {
    val options = this.split("|").map { it.trim() }
    return FloatingSharingButtonOptions(
        onScoreChanges = "1" in options,
        onStatusChanges = "2" in options,
        onProgressChanges = "3" in options
    )
}
