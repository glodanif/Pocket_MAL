package com.g.pocketmal.ui.recommendations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.g.pocketmal.argument
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.transformedArgument
import com.g.pocketmal.ui.SkeletonActivity
import com.g.pocketmal.ui.TitleDetailsActivity
import com.g.pocketmal.ui.theme.PocketMalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationsActivity : SkeletonActivity() {

    private val type by transformedArgument<Int, TitleType>(EXTRA_TYPE, TitleType.ANIME) {
        TitleType.from(it)
    }
    private val id by argument<Int>(EXTRA_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                RecommendationsContent(
                    titleId = id,
                    titleType = type,
                    onBackPressed = { finish() },
                )
            }
        }
    }

    companion object {

        private const val EXTRA_ID = "extra.id"
        private const val EXTRA_TYPE = "extra.type"

        fun start(context: Context, id: Int, type: TitleType) {
            val intent = Intent(context, RecommendationsActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            intent.putExtra(EXTRA_TYPE, type.type)
            context.startActivity(intent)
        }
    }
}

@Composable
private fun RecommendationsContent(
    titleId: Int?,
    titleType: TitleType,
    viewModel: RecommendationsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {

    val context = LocalContext.current
    val recommendationsState by viewModel.recommendationsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecommendations(titleId, titleType)
    }

    RecommendationsScreen(
        recommendationsState = recommendationsState,
        onBackPressed = onBackPressed,
        onRetryPressed = {
            viewModel.loadRecommendations(titleId, titleType)
        },
        onRecommendationClick = { id ->
            TitleDetailsActivity.start(context, id, titleType)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecommendationsScreen(
    recommendationsState: RecommendationsState,
    onRetryPressed: () -> Unit,
    onBackPressed: () -> Unit,
    onRecommendationClick: (Int) -> Unit,
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
    ) { innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (recommendationsState) {
                is RecommendationsState.RecommendationsList -> RecommendationsList(
                    recommendations = recommendationsState.recommendations,
                    onRecommendationClick = onRecommendationClick,
                )

                RecommendationsState.NoRecommendations -> NoRecommendationsView()
                RecommendationsState.Loading -> LoadingView()
                is RecommendationsState.FailedToLoad -> FailedToLoadView(
                    failedState = recommendationsState,
                    onRetryPressed = onRetryPressed,
                )

                RecommendationsState.IncorrectInput -> IncorrectInputView()
            }
        }
    }
}

@Composable
private fun RecommendationsList(
    recommendations: List<RecommendedTitleViewEntity>,
    onRecommendationClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(recommendations.size) { index ->
            RecommendationItem(
                recommendation = recommendations[index],
                onClick = onRecommendationClick,
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
            .height(128.dp)
            .shadow(
                elevation = 8.dp,
                shape = CardDefaults.shape,
                spotColor = Color.Black
            ),
        onClick = { onClick(recommendation.id) }
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(88.dp),
                model = recommendation.poster,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            ) {
                Text(
                    text = recommendation.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                )
                Text(
                    text = recommendation.recommendationsCount,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = recommendation.details,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun LoadingView() {
    CircularProgressIndicator(
        modifier = Modifier.size(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
private fun IncorrectInputView() {
    Box(
        modifier = Modifier.padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Unable to get the title id, please relaunch the app...",
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun NoRecommendationsView() {
    Box(
        modifier = Modifier.padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "No recommendations for this title", textAlign = TextAlign.Center)
    }
}

@Composable
private fun FailedToLoadView(
    failedState: RecommendationsState.FailedToLoad,
    onRetryPressed: () -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = failedState.errorMessage, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetryPressed) {
            Text(text = "Try again")
        }
    }
}