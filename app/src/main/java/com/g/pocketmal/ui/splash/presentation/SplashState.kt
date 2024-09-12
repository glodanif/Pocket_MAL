package com.g.pocketmal.ui.splash.presentation

sealed class SplashState {
    data object Waiting : SplashState()
    data object LoggedIn : SplashState()
    data object NotLoggedIn : SplashState()
}
