package com.g.pocketmal.ui.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.keyvalue.UserSettings
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
}
