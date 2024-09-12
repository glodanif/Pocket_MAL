package com.g.pocketmal.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.g.pocketmal.ui.legacy.ListActivity
import com.g.pocketmal.ui.login.LoginActivity
import com.g.pocketmal.ui.splash.presentation.SplashState
import com.g.pocketmal.ui.splash.presentation.SplashViewModel
import com.g.pocketmal.ui.theme.PocketMalTheme
import com.g.pocketmal.ui.theme.primaryLight
import dagger.hilt.android.AndroidEntryPoint

//TODO: Move this to the Home screen as Android add
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                SplashScreen(
                    onLoginRedirect = {
                        val intent = Intent(this, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        }
                        startActivity(intent)
                        finish()
                    },
                    onListRedirect = {
                        val intent = Intent(this, ListActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        }
                        startActivity(intent)
                        finish()
                    },
                )
            }
        }
    }
}

@Composable
private fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onListRedirect: () -> Unit,
    onLoginRedirect: () -> Unit,
) {
    val sessionState by viewModel.sessionState.collectAsState()

    LaunchedEffect(sessionState) {
        if (sessionState == SplashState.LoggedIn) {
            onListRedirect()
        } else if (sessionState == SplashState.NotLoggedIn) {
            onLoginRedirect()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryLight)
    )
}
