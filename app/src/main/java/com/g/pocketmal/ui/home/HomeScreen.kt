package com.g.pocketmal.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.LibraryAdd
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.list.ListScreen
import com.g.pocketmal.ui.search.SearchScreen
import com.g.pocketmal.ui.userprofile.UserProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data object AnimeList

@Serializable
data object MangaList

@Serializable
data object Seasonal

@Serializable
data object Ranked

@Serializable
data object Browse

@Serializable
data object Search

@Serializable
data object UserProfile

@Composable
fun HomeScreen(
    onSettingsClicked: () -> Unit,
    onAboutClicked: () -> Unit,
    onTitleClicked: (Int, TitleType) -> Unit,
) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Rounded.VideoLibrary, contentDescription = "Anime List") },
                    label = { Text("Anime") },
                    selected = false,//navController.currentBackStackEntry?.toRoute<AnimeList>() == AnimeList,
                    onClick = {
                        navController.navigate(AnimeList) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Rounded.MenuBook,
                            contentDescription = "Manga List"
                        )
                    },
                    label = { Text("Manga") },
                    selected = false,//navController.currentBackStackEntry?.toRoute<MangaList>() == MangaList,
                    onClick = {
                        navController.navigate(MangaList) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Rounded.LibraryAdd, contentDescription = "Browse") },
                    label = { Text("Browse") },
                    selected = false,//navController.currentBackStackEntry?.toRoute<Browse>() == Browse,
                    onClick = {
                        navController.navigate(Browse) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Rounded.Search, contentDescription = "Search") },
                    label = { Text("Search") },
                    selected = false,//navController.currentBackStackEntry?.toRoute<Search>() == Search,
                    onClick = {
                        navController.navigate(Search) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Rounded.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,//navController.currentBackStackEntry?.toRoute<UserProfile>() == UserProfile,
                    onClick = {
                        navController.navigate(UserProfile) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPaddings ->
        NavHost(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize(),
            navController = navController,
            startDestination = AnimeList
        ) {
            composable<AnimeList> {
                ListScreen(
                    titleType = TitleType.ANIME,
                    onRecordClicked = { id, type ->

                    }
                )
            }
            composable<MangaList> {
                ListScreen(
                    titleType = TitleType.MANGA,
                    onRecordClicked = { id, type ->

                    }
                )
            }
            composable<Seasonal> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                )
            }
            composable<Ranked> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Magenta)
                )
            }
            composable<Browse> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                )
            }
            composable<Search> {
                SearchScreen(
                    onSearchItemClick = { id, type ->
                        onTitleClicked(id, type)
                    }
                )
            }
            composable<UserProfile> {
                UserProfileScreen(
                    userId = 0,
                    onLoggedOut = {

                    },
                )
            }
        }
    }
}
