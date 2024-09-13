package com.g.pocketmal.data.keyvalue

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.ThemeMode
import com.g.pocketmal.domain.entity.UserSettingsEntity
import com.g.pocketmal.util.Action

class UserSettings(context: Context) {

    //TODO: DI
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun getUserSettings(): UserSettingsEntity {
        return UserSettingsEntity(
            showNsfwContentInList = getDisplayNsfwInList(),
            showNsfwContentInExplore = getDisplayNsfw(),
            themeMode = getThemeMode(),
            englishTitles = getShowEnglishTitles(),
            listAutoSync = isAutoSyncEnabled(),
            defaultList = getDefaultList(),
        )
    }

    fun isSimpleViewMode(): Boolean =
        preferences.getBoolean(SIMPLE_VIEW_KEY, false)


    fun isSortingStateSaving(): Boolean =
        preferences.getBoolean(SAVE_SORTING_STATE_KEY, false)

    fun isShowActionPopup(): Boolean =
        preferences.getBoolean(ACTION_POPUP_KEY, true)


    fun useExternalBrowser(): Boolean {
        return preferences.getBoolean(EXTERNAL_BROWSER_KEY, false)
    }

    fun showTagsInList(): Boolean =
        preferences.getBoolean(TAGS_IN_LIST_KEY, false)


    fun shouldShowPopup(
        actionType: Action,
        title: com.g.pocketmal.ui.legacy.viewentity.RecordViewModel
    ): Boolean {

        if (actionType == Action.ACTION_SHARE) {
            return true
        }

        if (!isShowActionPopup()) {
            return false
        }

        val options = preferences.getString(ACTION_POPUP_OPTIONS_KEY, "1|2|3") ?: "1|2|3"

        return when (actionType) {
            Action.ACTION_EPISODES, Action.ACTION_CHAPTERS, Action.ACTION_VOLUMES -> options.contains(
                "1"
            )
            Action.ACTION_SCORE -> options.contains("2") && title.myScore > 0
            Action.ACTION_STATUS, Action.ACTION_REWATCHED -> options.contains("3")
            else -> false
        }
    }





    fun getDefaultList(): TitleType {
        return when (preferences.getString(DEFAULT_LIST_KEY, "1")) {
            "3" -> TitleType.from(preferences.getInt(LAST_OPENED_LIST_KEY, 0))
            "1" -> TitleType.ANIME
            else -> TitleType.MANGA
        }
    }

    fun setDefaultList(type: TitleType) {

    }

    fun setLastOpenedList(type: TitleType) {
        preferences.edit {
            putInt(LAST_OPENED_LIST_KEY, type.type)
        }
    }

    fun isAutoSyncEnabled(): Boolean =
        preferences.getBoolean(AUTO_SYNC_KEY, UserSettingsEntity.default.listAutoSync)

    fun setAutoSyncEnabled(enabled: Boolean) =
        preferences.edit {
            putBoolean(AUTO_SYNC_KEY, enabled)
        }

    fun getShowEnglishTitles(): Boolean =
        preferences.getBoolean(ENGLISH_TITLES, UserSettingsEntity.default.englishTitles)

    fun setShowEnglishTitles(enabled: Boolean) {
        preferences.edit {
            putBoolean(ENGLISH_TITLES, enabled)
        }
    }

    fun getThemeMode(): ThemeMode {
        val defaultTheme = UserSettingsEntity.default.themeMode
        val theme = preferences.getString(THEME_KEY, defaultTheme.mode)
        return ThemeMode.from(theme)
    }

    fun setThemeMode(themeMode: ThemeMode) {
        preferences.edit {
            putString(THEME_KEY, themeMode.mode)
        }
    }

    fun getDisplayNsfw(): Boolean =
        preferences.getBoolean(
            HENTAI_FILTER_BROWSE_KEY,
            UserSettingsEntity.default.showNsfwContentInExplore,
        )

    fun setDisplayNsfw(enabled: Boolean) {
        preferences.edit {
            putBoolean(HENTAI_FILTER_BROWSE_KEY, enabled)
        }
    }

    fun getDisplayNsfwInList(): Boolean =
        preferences.getBoolean(
            HENTAI_FILTER_LIST_KEY,
            UserSettingsEntity.default.showNsfwContentInList,
        )

    fun setDisplayNsfwInList(enabled: Boolean) {
        preferences.edit {
            putBoolean(HENTAI_FILTER_LIST_KEY, enabled)
        }
    }

    fun migrate() {
        if (preferences.getBoolean("darkTheme", false)) {
            preferences.edit {
                putString("theme", ThemeMode.DARK.mode)
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
