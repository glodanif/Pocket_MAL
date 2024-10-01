package com.g.pocketmal.ui.login.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
) :ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.LoadingWebPage)
    val loginState = _loginState.asStateFlow()

    private var codeVerifier: String = ""
    private var redirectUrl: String = ""


    init {
        loadAuthPageUrlData()
    }

    fun loadAuthPageUrlData() {
        try {
            val authData = sessionRepository.generateAuthData()
            codeVerifier = authData.codeVerifier
            redirectUrl = authData.redirectUrl
            _loginState.value = LoginState.AuthDataReady(authData.url)
        } catch (e: IOException) {
            _loginState.value = LoginState.NoInternet
        } catch (e: Exception) {
            _loginState.value = LoginState.Error(e.message ?: "")
        }
    }

    fun handleAuthPageLoaded() {
        _loginState.value = LoginState.AuthPageReady
    }

    fun findCode(url: String): String? {
        try {
            val code = Uri.parse(url).getQueryParameter("code")
            return if (!url.contains(redirectUrl) || code.isNullOrEmpty()) {
                null
            } else {
                code
            }
        } catch (e: UnsupportedOperationException) {
            return null
        }
    }

    fun authorize(code: String) {
        _loginState.value = LoginState.Authorizing
        viewModelScope.launch {
            try {
                sessionRepository.authorize(code, codeVerifier)
                _loginState.value = LoginState.Authorized
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "")
            }
        }
    }
}
