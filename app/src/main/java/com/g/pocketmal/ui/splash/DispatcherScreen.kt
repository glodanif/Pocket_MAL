package com.g.pocketmal.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.g.pocketmal.ui.splash.presentation.SplashState
import com.g.pocketmal.ui.splash.presentation.SplashViewModel
import com.g.pocketmal.ui.theme.primaryLight

@Composable
fun DispatcherScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onListLoaded: () -> Unit,
    onNotLoggedIn: () -> Unit,
) {
    val sessionState by viewModel.sessionState.collectAsState()

    LaunchedEffect(sessionState) {
        if (sessionState == SplashState.LoggedIn) {
            onListLoaded()
        } else if (sessionState == SplashState.NotLoggedIn) {
            onNotLoggedIn()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryLight)
    )
}
