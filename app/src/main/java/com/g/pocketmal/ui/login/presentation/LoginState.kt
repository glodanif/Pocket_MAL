package com.g.pocketmal.ui.login.presentation

sealed class LoginState {
    data object LoadingWebPage : LoginState()
    data object Authorizing : LoginState()
    data object Authorized : LoginState()
    data class AuthDataReady(val url: String) : LoginState()
    data object AuthPageReady : LoginState()
    data object NoInternet : LoginState()
    data class Error(val message: String) : LoginState()
}
