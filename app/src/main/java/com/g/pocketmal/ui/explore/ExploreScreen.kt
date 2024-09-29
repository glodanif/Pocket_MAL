package com.g.pocketmal.ui.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.g.pocketmal.domain.TitleType

@Composable
fun ExploreScreen(
    onSeasonalAnimeClicked: () -> Unit,
    onTopRatedClicked: (TitleType) -> Unit,
    onMostPopularClicked: (TitleType) -> Unit,
    onReleasingClicked: (TitleType) -> Unit,
    onUpcomingClicked: (TitleType) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
