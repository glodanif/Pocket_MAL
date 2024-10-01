package com.g.pocketmal.ui.externallinks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExternalLinksViewModel @Inject constructor(
    private val repository: UserSettingsRepository
): ViewModel() {

    private val _pattern = MutableStateFlow("")
    val pattern = _pattern.asStateFlow()

    init {
        loadExternalLinksPattern()
    }

    fun saveNewPattern(pattern: String) {
        viewModelScope.launch {
            repository.saveNewPattern(pattern)
        }
    }

    private fun loadExternalLinksPattern() {
        viewModelScope.launch {
            repository.getExternalLinkPatternFlow().collect { preferences ->
                _pattern.value = preferences.externalLinkPattern
            }
        }
    }
}
