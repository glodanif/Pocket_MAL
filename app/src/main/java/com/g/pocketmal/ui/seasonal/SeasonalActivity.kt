package com.g.pocketmal.ui.seasonal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.ui.common.ErrorMessageView
import com.g.pocketmal.ui.common.ErrorMessageWithRetryView
import com.g.pocketmal.ui.common.LoadingView
import com.g.pocketmal.ui.common.ScoreLabel
import com.g.pocketmal.ui.common.SmallDetailsRow
import com.g.pocketmal.ui.common.inliststatus.InListStatusLabel
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.legacy.TitleDetailsActivity
import com.g.pocketmal.ui.seasonal.presentation.Season
import com.g.pocketmal.ui.seasonal.presentation.SeasonalAnimeViewEntity
import com.g.pocketmal.ui.seasonal.presentation.SeasonalSectionViewEntity
import com.g.pocketmal.ui.seasonal.presentation.SeasonalState
import com.g.pocketmal.ui.seasonal.presentation.SeasonalViewModel
import com.g.pocketmal.ui.theme.PocketMalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeasonalActivity : SkeletonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                SeasonalContent(
                    onAnimeClicked = { id ->
                        TitleDetailsActivity.start(this, id, TitleType.ANIME)
                    },
                    onBackPressed = { finish() },
                )
            }
        }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SeasonalActivity::class.java))
        }
    }
}

@Composable
private fun SeasonalContent(
    viewModel: SeasonalViewModel = hiltViewModel(),
    onAnimeClicked: (Int) -> Unit,
    onBackPressed: () -> Unit,
) {

    val season by viewModel.season.collectAsState()
    val seasonalState by viewModel.seasonalState.collectAsState()

    LaunchedEffect(season) {
        viewModel.loadSeason(season)
    }

    SeasonalScreen(
        season = season,
        seasonalState = seasonalState,
        onBackPressed = onBackPressed,
        onRetryPressed = {
            viewModel.loadSeason(season)
        },
        onAnimeClicked = onAnimeClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonalScreen(
    season: Season,
    seasonalState: SeasonalState,
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
                    Text(
                        modifier = Modifier.clickable { isSeasonPickerOpened = true },
                        text = season.year.toString(),
                    )
                }
            )
        },
    ) { innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
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
            onApplyClicked = {},
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
        contentPadding = PaddingValues(16.dp)
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
    Text(text = title)
}

@Composable
private fun SeasonalItem(
    seasonalItem: SeasonalAnimeViewEntity,
    onClicked: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = CardDefaults.shape,
                spotColor = Color.Black
            ),
        onClick = { onClicked(seasonalItem.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .height(164.dp)
                        .width(113.dp),
                    model = seasonalItem.poster,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = seasonalItem.title,
                        style = MaterialTheme.typography.titleMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = seasonalItem.genres,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
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
                            if (seasonalItem.airing != null) {
                                Text(
                                    text = seasonalItem.airing,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                        ScoreLabel(
                            modifier = Modifier.wrapContentWidth(Alignment.End),
                            score = seasonalItem.score,
                        )
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonSelectorBottomSheet(
    selectedSeason: Season,
    onDismissRequest: () -> Unit,
    onApplyClicked: (Season) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()
    val season by remember { mutableStateOf(selectedSeason) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = selectedSeason.toString())

            Button(onClick = {
                onApplyClicked(season)
            }) {
                Text(text = "Apply")
            }
        }
    }
}
