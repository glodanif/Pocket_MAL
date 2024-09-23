package com.g.pocketmal.ui.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.data.repository.ListRepository
import com.g.pocketmal.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val settings: UserSettings,
    private val sessionRepository: SessionRepository,
    private val listRepository: ListRepository,
) : ViewModel() {

    private val _sessionState = MutableStateFlow<SplashState>(SplashState.Waiting)
    val sessionState = _sessionState.asStateFlow()

    init {
        migrate()
        dispatch()
    }

    private fun migrate() {
        settings.migrate()
    }

    private fun dispatch() {
        viewModelScope.launch {
            if (sessionRepository.isUserLoggedIn()) {
                //listRepository.loadListFromDb()
                _sessionState.value = SplashState.LoggedIn
            } else {
                _sessionState.value = SplashState.NotLoggedIn
            }
        }
    }
}
