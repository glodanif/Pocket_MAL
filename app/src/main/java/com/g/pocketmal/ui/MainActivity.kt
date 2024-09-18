package com.g.pocketmal.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.home.HomeScreen
import com.g.pocketmal.ui.login.LoginScreen
import com.g.pocketmal.ui.splash.DispatcherScreen
import com.g.pocketmal.ui.theme.PocketMalTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                PocketMalApp()
            }
        }
    }
}

@Serializable
data object Dispatcher

@Serializable
data object Login

@Serializable
data object Home

@Serializable
data class Details(val id: Int, val type: TitleType)

@Serializable
data object Settings

@Serializable
data object About

@Composable
private fun PocketMalApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Dispatcher) {
        composable<Dispatcher> {
            DispatcherScreen(
                onListLoaded = {
                    navController.navigate(Home) {
                        popUpTo(Dispatcher) { inclusive = true }
                    }
                },
                onNotLoggedIn = {
                    navController.navigate(Login) {
                        popUpTo(Dispatcher) { inclusive = true }
                    }
                },
            )
        }
        composable<Login> {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }
        composable<Home> {
            HomeScreen(
                onSettingsClicked = {
                    navController.navigate(Settings)
                },
                onAboutClicked = {
                    navController.navigate(About)
                },
                onTitleClicked = { id, type ->
                    navController.navigate(Details(id, type))
                }
            )
        }
    }
}