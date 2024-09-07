package com.g.pocketmal.ui.externallinks.presentation

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.keyvalue.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExternalLinksViewModel @Inject constructor(
    private val userPreferencesStorage: DataStore<UserPreferences>
): ViewModel() {

    private val _pattern = MutableStateFlow("")
    val pattern = _pattern.asStateFlow()

    init {
        loadExternalLinksPattern()
    }

    fun saveNewPattern(pattern: String) {
        viewModelScope.launch {
            userPreferencesStorage.updateData { preferences ->
                preferences.copy(externalLinkPattern = pattern)
            }
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
