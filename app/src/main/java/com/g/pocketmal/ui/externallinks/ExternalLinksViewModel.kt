package com.g.pocketmal.ui.externallinks

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.keyvalue.UserPreferences
import com.g.pocketmal.ui.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExternalLinksViewModel @Inject constructor(
    private val mainSettings: MainSettings,
    private val userPreferencesStorage: DataStore<UserPreferences>
): ViewModel() {

    private val _themeMode = MutableStateFlow(ThemeMode.LIGHT)
    val themeMode = _themeMode.asStateFlow()
    private val _pattern = MutableStateFlow("")
    val pattern = _pattern.asStateFlow()

    init {
        loadThemeMode()
        loadExternalLinksPattern()
    }

    fun saveNewPattern(pattern: String) {
        viewModelScope.launch {
            userPreferencesStorage.updateData { preferences ->
                preferences.copy(externalLinkPattern = pattern)
            }
        }
    }

    private fun loadThemeMode() {
        viewModelScope.launch {
            val mode = mainSettings.getTheme()
            _themeMode.value = ThemeMode.fromTheme(mode)
        }
    }

    private fun loadExternalLinksPattern() {
        viewModelScope.launch {
            userPreferencesStorage.data.collect { preferences ->
                _pattern.value = preferences.externalLinkPattern
            }
        }
    }
}
