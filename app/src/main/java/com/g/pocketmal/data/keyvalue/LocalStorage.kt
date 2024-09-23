package com.g.pocketmal.data.keyvalue

import android.content.Context
import androidx.core.content.edit
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.SortingType

class LocalStorage(context: Context) {

    private val preferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)

    fun storeLastSynchronizing(date: Long, type: TitleType) {
        preferences.edit {
            putLong(if (type === TitleType.ANIME) ANIME_LAST_SYNCHRONIZING else MANGA_LAST_SYNCHRONIZING, date)
        }
    }

    fun getLastSynchronizing(type: TitleType): Long {
        return preferences.getLong(if (type === TitleType.ANIME) ANIME_LAST_SYNCHRONIZING else MANGA_LAST_SYNCHRONIZING, 0)
    }

    fun getSortingType() = SortingType.from(preferences.getInt(SORTING_TYPE, 0))

    fun storeSortingType(type: SortingType) {
        preferences.edit {
            putInt(SORTING_TYPE, type.type)
        }
    }

    fun getSortingReverse() = preferences.getBoolean(SORTING_REVERSE, false)

    fun storeSortingReverse(reverse: Boolean) {
        preferences.edit {
            putBoolean(SORTING_REVERSE, reverse)
        }
    }

    fun reset() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val ANIME_LAST_SYNCHRONIZING = "mal_anime_last_synchronizing"
        private const val MANGA_LAST_SYNCHRONIZING = "mal_manga_last_synchronizing"
        private const val SORTING_TYPE = "sorting_type"
        private const val SORTING_REVERSE = "sorting_reverse"
        private const val KEY_TOTAL_FRIENDS = "total_friends"
        private const val PREFERENCES_KEY = "LocalStoring"
    }
}