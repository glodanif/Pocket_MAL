package com.g.pocketmal.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.keyvalue.MainSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenViewModel @Inject constructor(
    private val mainSettings: MainSettings,
) : ViewModel() {

    private val _themeMode = MutableStateFlow(ThemeMode.LIGHT)
    val themeMode = _themeMode.asStateFlow()

    init {
        loadThemeMode()
    }

    private fun loadThemeMode() {
        viewModelScope.launch {
            val mode = mainSettings.getTheme()
            _themeMode.value = ThemeMode.fromTheme(mode)
        }
    }
}
