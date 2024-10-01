package com.g.pocketmal.data.repository

import androidx.datastore.core.DataStore
import com.g.pocketmal.data.DataDefaultList
import com.g.pocketmal.data.DataThemeMode
import com.g.pocketmal.data.keyvalue.DataUserPreferences
import com.g.pocketmal.data.keyvalue.UserSettingsStorage
import com.g.pocketmal.domain.DefaultList
import com.g.pocketmal.domain.FloatingSharingButtonOptions
import com.g.pocketmal.domain.ThemeType
import com.g.pocketmal.domain.UserPreferences
import com.g.pocketmal.domain.entity.UserSettings
import com.g.pocketmal.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSettingsRepositoryImplementation(
    private val storage: UserSettingsStorage,
    private val userPreferencesStorage: DataStore<DataUserPreferences>
) : UserSettingsRepository {

    override fun getExternalLinkPatternFlow(): Flow<UserPreferences> {
        return userPreferencesStorage.data.map { preferences ->
            UserPreferences(
                externalLinkPattern = preferences.externalLinkPattern
            )
        }
    }

    override fun getThemeType(): ThemeType {
        return storage.getThemeMode().toDomain()
    }

    override fun migrateStorage() {
        storage.migrate()
    }

    override suspend fun getUserSettings(): UserSettings {
        return storage.getUserSettings()
    }

    override suspend fun setNewNsfwInListValue(enabled: Boolean) {
        storage.setDisplayNsfwInList(enabled)
    }

    override suspend fun setNewNsfwInExploreValue(enabled: Boolean) {
        storage.setDisplayNsfw(enabled)
    }

    override suspend fun setNewShowEnglishTitlesValue(enabled: Boolean) {
        storage.setShowEnglishTitles(enabled)
    }

    override suspend fun setNewAutoSyncValue(enabled: Boolean) {
        storage.setAutoSyncEnabled(enabled)
    }

    override suspend fun setNewSaveSortingOrderValue(enabled: Boolean) {
        storage.setSaveSortingOrderEnabled(enabled)
    }

    override suspend fun setNewThemeValue(theme: ThemeType) {
        storage.setThemeMode(DataThemeMode.from(theme))
    }

    override suspend fun setNewDefaultListValue(list: DefaultList) {
        storage.setDefaultList(DataDefaultList.from(list))
    }

    override suspend fun setNewUseExternalBrowserValue(enabled: Boolean) {
        storage.setUseExternalBrowserEnabled(enabled)
    }

    override suspend fun setNewHidePostersInListValue(enabled: Boolean) {
        storage.setHidePostersInListEnabled(enabled)
    }

    override suspend fun setNewShowTagsInListValue(enabled: Boolean) {
        storage.setShowTagsInListEnabled(enabled)
    }

    override suspend fun setNewEnableFloatingSharingButtonValue(enabled: Boolean) {
        storage.setFloatingSharingButtonEnabled(enabled)
    }

    override suspend fun setNewFloatingSharingButtonOptionsValue(options: FloatingSharingButtonOptions) {
        storage.setFloatingSharingButtonOptions(options)
    }

    override suspend fun saveNewPattern(pattern: String) {
        userPreferencesStorage.updateData { preferences ->
            preferences.copy(externalLinkPattern = pattern)
        }
    }
}
