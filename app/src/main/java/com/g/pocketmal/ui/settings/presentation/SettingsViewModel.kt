package com.g.pocketmal.ui.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.domain.DefaultList
import com.g.pocketmal.domain.FloatingSharingButtonOptions
import com.g.pocketmal.domain.ThemeMode
import com.g.pocketmal.domain.entity.UserSettingsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettings: UserSettings,
) : ViewModel() {

    private val _settingsState = MutableStateFlow(UserSettingsEntity.default)
    val settingsState = _settingsState.asStateFlow()

    init {
        loadUserSettings()
    }

    private fun loadUserSettings() {
        viewModelScope.launch {
            val settings = userSettings.getUserSettings()
            _settingsState.value = settings
        }
    }

    fun setNewNsfwInListValue(enabled: Boolean) {
        viewModelScope.launch {
            userSettings.setDisplayNsfwInList(enabled)
            _settingsState.value = _settingsState.value.copy(showNsfwContentInList = enabled)
        }
    }

    fun setNewNsfwInExploreValue(enabled: Boolean) {
        viewModelScope.launch {
            userSettings.setDisplayNsfw(enabled)
            _settingsState.value = _settingsState.value.copy(showNsfwContentInExplore = enabled)
        }
    }

    fun setNewShowEnglishTitlesValue(enabled: Boolean) {
        viewModelScope.launch {
            userSettings.setShowEnglishTitles(enabled)
            _settingsState.value = _settingsState.value.copy(englishTitles = enabled)
        }
    }

    fun setNewAutoSyncValue(enabled: Boolean) {
        viewModelScope.launch {
            userSettings.setAutoSyncEnabled(enabled)
            _settingsState.value = _settingsState.value.copy(listAutoSync = enabled)
        }
    }

    fun setNewSaveSortingOrderValue(enabled: Boolean) {
        viewModelScope.launch {
            userSettings.setSaveSortingOrderEnabled(enabled)
            _settingsState.value = _settingsState.value.copy(saveSortingOrder = enabled)
        }
    }

    fun setNewThemeValue(theme: ThemeMode) {
        viewModelScope.launch {
            userSettings.setThemeMode(theme)
            _settingsState.value = _settingsState.value.copy(themeMode = theme)
        }
    }

    fun setNewDefaultListValue(list: DefaultList) {
        viewModelScope.launch {
            userSettings.setDefaultList(list)
            _settingsState.value = _settingsState.value.copy(defaultList = list)
        }
    }

    fun setNewUseExternalBrowserValue(enabled: Boolean) {
        viewModelScope.launch {
            userSettings.setUseExternalBrowserEnabled(enabled)
            _settingsState.value = _settingsState.value.copy(useExternalBrowser = enabled)
        }
    }

    fun setNewHidePostersInListValue(enabled: Boolean) {
        viewModelScope.launch {
            userSettings.setHidePostersInListEnabled(enabled)
            _settingsState.value = _settingsState.value.copy(hidePostersInList = enabled)
        }
    }

    fun setNewShowTagsInListValue(enabled: Boolean) {
        viewModelScope.launch {
            userSettings.setShowTagsInListEnabled(enabled)
            _settingsState.value = _settingsState.value.copy(showTagsInList = enabled)
        }
    }

    fun setNewEnableFloatingSharingButtonValue(enabled: Boolean) {
        viewModelScope.launch {
            userSettings.setFloatingSharingButtonEnabled(enabled)
            _settingsState.value = _settingsState.value.copy(enableFloatingSharingButton = enabled)
        }
    }

    fun setNewFloatingSharingButtonOptionsValue(options: FloatingSharingButtonOptions) {
        viewModelScope.launch {
            userSettings.setFloatingSharingButtonOptions(options)
            _settingsState.value = _settingsState.value.copy(floatingSharingButtonOptions = options)
        }
    }
}
