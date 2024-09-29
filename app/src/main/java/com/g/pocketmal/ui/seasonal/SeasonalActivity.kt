package com.g.pocketmal.ui.seasonal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.g.pocketmal.ui.common.ErrorMessageView
import com.g.pocketmal.ui.common.ErrorMessageWithRetryView
import com.g.pocketmal.ui.common.LoadingView
import com.g.pocketmal.ui.common.Poster
import com.g.pocketmal.ui.common.ScoreLabel
import com.g.pocketmal.ui.common.inliststatus.InListStatusLabel
import com.g.pocketmal.ui.seasonal.presentation.Season
import com.g.pocketmal.ui.seasonal.presentation.SeasonalAnimeViewEntity
import com.g.pocketmal.ui.seasonal.presentation.SeasonalSectionViewEntity
import com.g.pocketmal.ui.seasonal.presentation.SeasonalState
import com.g.pocketmal.ui.seasonal.presentation.SeasonalViewModel

@Composable
fun SeasonalScreen(
    viewModel: SeasonalViewModel = hiltViewModel(),
    onAnimeClicked: (Int) -> Unit,
    onBackPressed: () -> Unit,
) {

    val season by viewModel.season.collectAsState()
    val seasonalState by viewModel.seasonalState.collectAsState()

    LaunchedEffect(season) {
        viewModel.loadSeason(season)
    }

    SeasonalContent(
        season = season,
        seasonalState = seasonalState,
        onBackPressed = onBackPressed,
        onRetryPressed = {
            viewModel.loadSeason(season)
        },
        onAnimeClicked = onAnimeClicked,
        onSeasonSelected = { newSeason ->
            viewModel.setNewSeason(newSeason)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonalContent(
    season: Season,
    seasonalState: SeasonalState,
    onSeasonSelected: (Season) -> Unit,
    onRetryPressed: () -> Unit,
    onBackPressed: () -> Unit,
    onAnimeClicked: (Int) -> Unit,
) {
    var isSeasonPickerOpened by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seasonal Anime") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { isSeasonPickerOpened = true }
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = season.partOfYear.toString(),
                            style = MaterialTheme.typography.titleMedium
                                .copy(color = MaterialTheme.colorScheme.onPrimary),
                        )
                        Text(
                            text = season.year.toString(),
                            style = MaterialTheme.typography.labelSmall
                                .copy(color = MaterialTheme.colorScheme.onPrimary),
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .consumeWindowInsets(innerPaddings)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                )
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (seasonalState) {
                SeasonalState.EmptySeason -> ErrorMessageView(message = "No titles in this season...")
                is SeasonalState.FailedToLoad -> ErrorMessageWithRetryView(
                    message = seasonalState.errorMessage,
                    onRetryClicked = onRetryPressed,
                )

                SeasonalState.Loading -> LoadingView()
                is SeasonalState.SeasonalList -> SeasonalList(
                    sections = seasonalState.sections,
                    onAnimeClicked = onAnimeClicked,
                )
            }
        }
    }

    if (isSeasonPickerOpened) {
        SeasonSelectorBottomSheet(
            selectedSeason = season,
            onDismissRequest = {
                isSeasonPickerOpened = false
            },
            onApplyClicked = { newSeason ->
                onSeasonSelected(newSeason)
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SeasonalList(
    sections: List<SeasonalSectionViewEntity>,
    onAnimeClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 64.dp)
    ) {
        sections.forEach { (name, content) ->
            stickyHeader {
                SeasonalSectionHeader(title = name)
            }

            items(content.size) { index ->
                SeasonalItem(
                    seasonalItem = content[index],
                    onClicked = onAnimeClicked,
                )
            }
        }
    }
}

@Composable
private fun SeasonalSectionHeader(title: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
    ) {
        Text(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp, top = 10.dp),
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
private fun SeasonalItem(
    seasonalItem: SeasonalAnimeViewEntity,
    onClicked: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = CardDefaults.shape,
            ),
        onClick = { onClicked(seasonalItem.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Poster(
                    modifier = Modifier
                        .height(164.dp)
                        .width(113.dp),
                    url = seasonalItem.poster,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(164.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = seasonalItem.title,
                        style = MaterialTheme.typography.titleMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                        ) {
                            if (seasonalItem.source != null) {
                                Text(
                                    text = seasonalItem.source,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                            if (seasonalItem.studio != null) {
                                Text(
                                    text = seasonalItem.studio,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            if (seasonalItem.episodes != null) {
                                Text(
                                    text = seasonalItem.episodes,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                            if (seasonalItem.airing != null) {
                                Text(
                                    text = seasonalItem.airing,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalAlignment = Alignment.End,
                        ) {
                            ScoreLabel(
                                modifier = Modifier.wrapContentWidth(Alignment.End),
                                score = seasonalItem.score,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    Icons.Rounded.People,
                                    contentDescription = "Members number icon",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = seasonalItem.members,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
            }
            val inMyListStatus = seasonalItem.inListStatus
            if (inMyListStatus.isInMyList) {
                InListStatusLabel(
                    status = inMyListStatus.statusLabel,
                    color = inMyListStatus.color,
                )
            }
            if (seasonalItem.synopsis.isNotEmpty() || seasonalItem.genres.isNotEmpty()) {
                if (!inMyListStatus.isInMyList) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outline),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                GenresRow(seasonalItem.genres)
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = seasonalItem.synopsis,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenresRow(
    genres: List<String>,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        genres.forEach { genre ->
            Text(
                modifier = Modifier
                    .padding(start = 2.dp, end = 2.dp, top = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 2.dp, horizontal = 8.dp),
                text = genre,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
