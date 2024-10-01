package com.g.pocketmal.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.domain.ThemeType
import com.g.pocketmal.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenViewModel @Inject constructor(
    private val repository: UserSettingsRepository,
) : ViewModel() {

    private val _themeType = MutableStateFlow(ThemeType.SYSTEM)
    val themeType = _themeType.asStateFlow()

    init {
        loadThemeMode()
    }

    private fun loadThemeMode() {
        viewModelScope.launch {
            _themeType.value = repository.getThemeType()
        }
    }
}
