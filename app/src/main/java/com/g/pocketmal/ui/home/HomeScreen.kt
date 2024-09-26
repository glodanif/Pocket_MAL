package com.g.pocketmal.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.LibraryAdd
import androidx.compose.material.icons.rounded.OndemandVideo
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.list.ListScreen
import com.g.pocketmal.ui.search.SearchScreen
import com.g.pocketmal.ui.userprofile.UserProfileScreen
import kotlinx.serialization.Serializable

sealed class HomeScreenTabs {
    @Serializable
    data object AnimeList : HomeScreenTabs()

    @Serializable
    data object MangaList : HomeScreenTabs()

    @Serializable
    data object Seasonal : HomeScreenTabs()

    @Serializable
    data object Ranked : HomeScreenTabs()

    @Serializable
    data object Browse : HomeScreenTabs()

    @Serializable
    data object Search : HomeScreenTabs()

    @Serializable
    data object UserProfile : HomeScreenTabs()
}

data class NavigationOption(
    val label: String,
    val icon: ImageVector,
    val screen: HomeScreenTabs,
)

@Composable
fun HomeScreen(
    onSettingsClicked: () -> Unit,
    onAboutClicked: () -> Unit,
    onTitleClicked: (Int, TitleType) -> Unit,
) {

    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf<HomeScreenTabs>(HomeScreenTabs.AnimeList) }

    val navigationOptions = listOf(
        NavigationOption(
            label = "Anime",
            icon = Icons.Rounded.OndemandVideo,
            screen = HomeScreenTabs.AnimeList,
        ),
        NavigationOption(
            label = "Manga",
            icon = Icons.AutoMirrored.Rounded.MenuBook,
            screen = HomeScreenTabs.MangaList,
        ),
        NavigationOption(
            label = "Browse",
            icon = Icons.Rounded.LibraryAdd,
            screen = HomeScreenTabs.Browse,
        ),
        NavigationOption(
            label = "Search",
            icon = Icons.Rounded.Search,
            screen = HomeScreenTabs.Search,
        ),
        NavigationOption(
            label = "Profile",
            icon = Icons.Rounded.Person,
            screen = HomeScreenTabs.UserProfile,
        )
    )

    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.surface),
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    navigationOptions.forEach { option ->
                        NavItem(
                            label = option.label,
                            icon = option.icon,
                            isSelected = selectedTab == option.screen,
                            onClicked = {
                                navController.navigate(option.screen) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                selectedTab = option.screen
                            }
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize(),
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = HomeScreenTabs.AnimeList
            ) {
                composable<HomeScreenTabs.AnimeList> {
                    ListScreen(
                        titleType = TitleType.ANIME,
                        onRecordClicked = { id, type ->

                        },
                    )
                }
                composable<HomeScreenTabs.MangaList> {
                    ListScreen(
                        titleType = TitleType.MANGA,
                        onRecordClicked = { id, type ->

                        }
                    )
                }
                composable<HomeScreenTabs.Seasonal> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                    )
                }
                composable<HomeScreenTabs.Ranked> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Magenta)
                    )
                }
                composable<HomeScreenTabs.Browse> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray)
                    )
                }
                composable<HomeScreenTabs.Search> {
                    SearchScreen(
                        onSearchItemClick = { id, type ->
                            onTitleClicked(id, type)
                        }
                    )
                }
                composable<HomeScreenTabs.UserProfile> {
                    UserProfileScreen(
                        userId = 0,
                        onLoggedOut = {

                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClicked: () -> Unit,
) {
    var modifier = Modifier
        .fillMaxHeight()
        .width(72.dp)

    if (isSelected) {
        modifier = modifier.background(MaterialTheme.colorScheme.inversePrimary)
    }

    Column(
        modifier = modifier.clickable {
            onClicked()
        },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(icon, contentDescription = "$label navigation bar icon")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}
