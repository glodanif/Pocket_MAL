package com.g.pocketmal.ui.recommendations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.common.ErrorMessageView
import com.g.pocketmal.ui.common.ErrorMessageWithRetryView
import com.g.pocketmal.ui.common.LoadingView
import com.g.pocketmal.ui.common.Poster
import com.g.pocketmal.ui.common.SmallDetailsRow
import com.g.pocketmal.ui.common.inliststatus.InListStatusLabel
import com.g.pocketmal.ui.recommendations.presentation.RecommendationsState
import com.g.pocketmal.ui.recommendations.presentation.RecommendationsViewModel
import com.g.pocketmal.ui.recommendations.presentation.RecommendedTitleViewEntity

@Composable
private fun RecommendationsScreen(
    titleId: Int,
    titleType: TitleType,
    viewModel: RecommendationsViewModel = hiltViewModel(),
    onRecommendationClicked: (Int, TitleType) -> Unit,
    onBackPressed: () -> Unit,
) {

    val recommendationsState by viewModel.recommendationsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecommendations(titleId, titleType)
    }

    RecommendationsContent(
        recommendationsState = recommendationsState,
        onBackPressed = onBackPressed,
        onRetryPressed = {
            viewModel.loadRecommendations(titleId, titleType)
        },
        onRecommendationClicked = { id ->
            onRecommendationClicked(id, titleType)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecommendationsContent(
    recommendationsState: RecommendationsState,
    onRetryPressed: () -> Unit,
    onBackPressed: () -> Unit,
    onRecommendationClicked: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recommendations") },
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
            when (recommendationsState) {
                is RecommendationsState.RecommendationsList -> RecommendationsList(
                    recommendations = recommendationsState.recommendations,
                    onRecommendationClicked = onRecommendationClicked,
                )

                RecommendationsState.NoRecommendations ->
                    ErrorMessageView(message = "No recommendations for this title")

                RecommendationsState.Loading -> LoadingView()

                is RecommendationsState.FailedToLoad ->
                    ErrorMessageWithRetryView(
                        message = recommendationsState.errorMessage,
                        onRetryClicked = onRetryPressed,
                    )

                RecommendationsState.IncorrectInput ->
                    ErrorMessageView(message = "Unable to get the title id, please relaunch the app...")
            }
        }
    }
}

@Composable
private fun RecommendationsList(
    recommendations: List<RecommendedTitleViewEntity>,
    onRecommendationClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 24.dp,
            bottom = 64.dp,
        ),
    ) {
        items(recommendations.size) { index ->
            RecommendationItem(
                recommendation = recommendations[index],
                onClick = onRecommendationClicked,
            )
        }
    }
}

@Composable
private fun RecommendationItem(
    recommendation: RecommendedTitleViewEntity,
    onClick: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = CardDefaults.shape,
                spotColor = Color.Black
            ),
        onClick = { onClick(recommendation.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Poster(
                    url = recommendation.poster,
                    modifier = Modifier
                        .height(128.dp)
                        .width(88.dp),
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = recommendation.title,
                        style = MaterialTheme.typography.titleMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = recommendation.recommendationsCount,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SmallDetailsRow(
                        score = recommendation.score,
                        details = recommendation.details,
                    )
                }
            }
            val inMyListStatus = recommendation.inListStatus
            if (inMyListStatus.isInMyList) {
                InListStatusLabel(
                    status = inMyListStatus.statusLabel,
                    color = inMyListStatus.color,
                )
            }
        }
    }
}
