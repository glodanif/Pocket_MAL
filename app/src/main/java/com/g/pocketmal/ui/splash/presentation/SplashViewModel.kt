package com.g.pocketmal.ui.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.domain.repository.SessionRepository
import com.g.pocketmal.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val settingsRepository: UserSettingsRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _sessionState = MutableStateFlow<SplashState>(SplashState.Waiting)
    val sessionState = _sessionState.asStateFlow()

    init {
        migrate()
        dispatch()
    }

    private fun migrate() {
        settingsRepository.migrateStorage()
    }

    private fun dispatch() {
        viewModelScope.launch {
            if (sessionRepository.isUserLoggedIn()) {
                _sessionState.value = SplashState.LoggedIn
            } else {
                _sessionState.value = SplashState.NotLoggedIn
            }
        }
    }
}
