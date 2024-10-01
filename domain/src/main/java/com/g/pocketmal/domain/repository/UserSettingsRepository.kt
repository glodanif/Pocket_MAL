package com.g.pocketmal.domain.repository

import com.g.pocketmal.domain.DefaultList
import com.g.pocketmal.domain.FloatingSharingButtonOptions
import com.g.pocketmal.domain.ThemeType
import com.g.pocketmal.domain.UserPreferences
import com.g.pocketmal.domain.entity.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    fun getExternalLinkPatternFlow(): Flow<UserPreferences>
    fun getThemeType(): ThemeType
    fun migrateStorage()
    suspend fun getUserSettings(): UserSettings
    suspend fun setNewNsfwInListValue(enabled: Boolean)
    suspend fun setNewNsfwInExploreValue(enabled: Boolean)
    suspend fun setNewShowEnglishTitlesValue(enabled: Boolean)
    suspend fun setNewAutoSyncValue(enabled: Boolean)
    suspend fun setNewSaveSortingOrderValue(enabled: Boolean)
    suspend fun setNewThemeValue(theme: ThemeType)
    suspend fun setNewDefaultListValue(list: DefaultList)
    suspend fun setNewUseExternalBrowserValue(enabled: Boolean)
    suspend fun setNewHidePostersInListValue(enabled: Boolean)
    suspend fun setNewShowTagsInListValue(enabled: Boolean)
    suspend fun setNewEnableFloatingSharingButtonValue(enabled: Boolean)
    suspend fun setNewFloatingSharingButtonOptionsValue(options: FloatingSharingButtonOptions)
    suspend fun saveNewPattern(pattern: String)
}
