package com.g.pocketmal.ui.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.g.pocketmal.domain.TitleType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    onSeasonalAnimeClicked: () -> Unit,
    onTopRatedClicked: (TitleType) -> Unit,
    onMostPopularClicked: (TitleType) -> Unit,
    onReleasingClicked: (TitleType) -> Unit,
    onUpcomingClicked: (TitleType) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPaddings ->
        Column(
            modifier = Modifier
                .padding(innerPaddings)
                .consumeWindowInsets(innerPaddings)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Explore Anime")
            Button(onClick = onSeasonalAnimeClicked) {
                Text(text = "Seasonal")
            }
            Button(
                onClick = {
                    onTopRatedClicked(TitleType.ANIME)
                },
            ) {
                Text(text = "Top Rated")
            }
            Button(
                onClick = {
                    onMostPopularClicked(TitleType.ANIME)
                },
            ) {
                Text(text = "Most Popular")
            }
            Button(
                onClick = {
                    onReleasingClicked(TitleType.ANIME)
                },
            ) {
                Text(text = "Airing")
            }
            Button(
                onClick = {
                    onUpcomingClicked(TitleType.ANIME)
                },
            ) {
                Text(text = "Upcoming")
            }
            Text(text = "Explore Manga")
            Button(
                onClick = {
                    onTopRatedClicked(TitleType.MANGA)
                },
            ) {
                Text(text = "Top Rated")
            }
            Button(
                onClick = {
                    onMostPopularClicked(TitleType.MANGA)
                },
            ) {
                Text(text = "Most Popular")
            }
            Button(
                onClick = {
                    onReleasingClicked(TitleType.MANGA)
                },
            ) {
                Text(text = "Publishing")
            }
            Button(
                onClick = {
                    onUpcomingClicked(TitleType.MANGA)
                },
            ) {
                Text(text = "Upcoming")
            }
        }
    }
}
