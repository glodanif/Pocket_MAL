package com.g.pocketmal.ui.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.domain.DefaultList
import com.g.pocketmal.domain.FloatingSharingButtonOptions
import com.g.pocketmal.domain.ThemeType
import com.g.pocketmal.domain.entity.UserSettings
import com.g.pocketmal.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UserSettingsRepository,
) : ViewModel() {

    private val _settingsState = MutableStateFlow(UserSettings.default)
    val settingsState = _settingsState.asStateFlow()

    init {
        loadUserSettings()
    }

    private fun loadUserSettings() {
        viewModelScope.launch {
            val settings = repository.getUserSettings()
            _settingsState.value = settings
        }
    }

    fun setNewNsfwInListValue(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNewNsfwInListValue(enabled)
            _settingsState.value = _settingsState.value.copy(showNsfwContentInList = enabled)
        }
    }

    fun setNewNsfwInExploreValue(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNewNsfwInExploreValue(enabled)
            _settingsState.value = _settingsState.value.copy(showNsfwContentInExplore = enabled)
        }
    }

    fun setNewShowEnglishTitlesValue(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNewShowEnglishTitlesValue(enabled)
            _settingsState.value = _settingsState.value.copy(englishTitles = enabled)
        }
    }

    fun setNewAutoSyncValue(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNewAutoSyncValue(enabled)
            _settingsState.value = _settingsState.value.copy(listAutoSync = enabled)
        }
    }

    fun setNewSaveSortingOrderValue(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNewSaveSortingOrderValue(enabled)
            _settingsState.value = _settingsState.value.copy(saveSortingOrder = enabled)
        }
    }

    fun setNewThemeValue(theme: ThemeType) {
        viewModelScope.launch {
            repository.setNewThemeValue(theme)
            _settingsState.value = _settingsState.value.copy(themeMode = theme)
        }
    }

    fun setNewDefaultListValue(list: DefaultList) {
        viewModelScope.launch {
            repository.setNewDefaultListValue(list)
            _settingsState.value = _settingsState.value.copy(defaultList = list)
        }
    }

    fun setNewUseExternalBrowserValue(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNewUseExternalBrowserValue(enabled)
            _settingsState.value = _settingsState.value.copy(useExternalBrowser = enabled)
        }
    }

    fun setNewHidePostersInListValue(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNewHidePostersInListValue(enabled)
            _settingsState.value = _settingsState.value.copy(hidePostersInList = enabled)
        }
    }

    fun setNewShowTagsInListValue(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNewShowTagsInListValue(enabled)
            _settingsState.value = _settingsState.value.copy(showTagsInList = enabled)
        }
    }

    fun setNewEnableFloatingSharingButtonValue(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNewEnableFloatingSharingButtonValue(enabled)
            _settingsState.value = _settingsState.value.copy(enableFloatingSharingButton = enabled)
        }
    }

    fun setNewFloatingSharingButtonOptionsValue(options: FloatingSharingButtonOptions) {
        viewModelScope.launch {
            repository.setNewFloatingSharingButtonOptionsValue(options)
            _settingsState.value = _settingsState.value.copy(floatingSharingButtonOptions = options)
        }
    }
}
