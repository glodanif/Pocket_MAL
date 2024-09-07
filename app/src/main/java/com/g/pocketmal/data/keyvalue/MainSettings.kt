package com.g.pocketmal.data.keyvalue

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import androidx.annotation.StringDef
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.util.Action

class MainSettings(context: Context) {

    private val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    fun defaultListType(): TitleType {
        return when (preferences.getString(DEFAULT_LIST_KEY, "1")) {
            "3" -> TitleType.from(preferences.getInt(LAST_OPENED_LIST_KEY, 0))
            "1" -> TitleType.ANIME
            else -> TitleType.MANGA
        }
    }

    fun isSimpleViewMode(): Boolean =
            preferences.getBoolean(SIMPLE_VIEW_KEY, false)

    @Theme
    fun getTheme(): String? = preferences.getString(THEME_KEY, LIGHT)

    fun isSortingStateSaving(): Boolean =
            preferences.getBoolean(SAVE_SORTING_STATE_KEY, false)

    fun isAutoSync(): Boolean =
            preferences.getBoolean(AUTO_SYNC_KEY, true)

    fun isShowActionPopup(): Boolean =
            preferences.getBoolean(ACTION_POPUP_KEY, true)

    fun setOpenedList(type: TitleType) {
        preferences.edit {
            putInt(LAST_OPENED_LIST_KEY, type.type)
        }
    }

    fun useExternalBrowser(): Boolean {
        return preferences.getBoolean(EXTERNAL_BROWSER_KEY, false)
    }

    fun showTagsInList(): Boolean =
            preferences.getBoolean(TAGS_IN_LIST_KEY, false)

    fun showEnglishTitles(): Boolean =
            preferences.getBoolean(ENGLISH_TITLES, false)

    fun displayNsfw(): Boolean =
            preferences.getBoolean(HENTAI_FILTER_BROWSE_KEY, false)

    fun displayNsfwInList(): Boolean =
            preferences.getBoolean(HENTAI_FILTER_LIST_KEY, true)

    fun shouldShowPopup(actionType: Action, title: com.g.pocketmal.ui.legacy.viewentity.RecordViewModel): Boolean {

        if (actionType == Action.ACTION_SHARE) {
            return true
        }

        if (!isShowActionPopup()) {
            return false
        }

        val options = preferences.getString(ACTION_POPUP_OPTIONS_KEY, "1|2|3") ?: "1|2|3"

        return when (actionType) {
            Action.ACTION_EPISODES, Action.ACTION_CHAPTERS, Action.ACTION_VOLUMES -> options.contains("1")
            //Action.ACTION_TAGS ->
            //    return options.contains("2") && title.myTags.isNotEmpty()
            Action.ACTION_SCORE -> options.contains("2") && title.myScore > 0
            Action.ACTION_STATUS, Action.ACTION_REWATCHED -> options.contains("3")
            else -> false
        }
    }

    fun migrate() {
        if (preferences.getBoolean("darkTheme", false)) {
            preferences.edit {
                putString("theme", DARK)
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

        const val LIGHT = "1"
        const val DARK = "2"
        const val BLACK = "3"

        @StringDef(LIGHT, DARK, BLACK)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Theme
    }
}
