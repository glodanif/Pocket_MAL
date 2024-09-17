package com.g.pocketmal.ui.ranked

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.transformedArgument
import com.g.pocketmal.ui.common.ErrorMessageView
import com.g.pocketmal.ui.common.ErrorMessageWithRetryView
import com.g.pocketmal.ui.common.LoadingView
import com.g.pocketmal.ui.common.Poster
import com.g.pocketmal.ui.common.SmallDetailsRow
import com.g.pocketmal.ui.common.inliststatus.InListStatusLabel
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.legacy.SkeletonToolbarActivity
import com.g.pocketmal.ui.legacy.TitleDetailsActivity
import com.g.pocketmal.ui.legacy.widget.LazyLoadFooter
import com.g.pocketmal.ui.ranked.presentation.RankedItemViewEntity
import com.g.pocketmal.ui.ranked.presentation.RankedState
import com.g.pocketmal.ui.ranked.presentation.RankedViewModel
import com.g.pocketmal.ui.recommendations.presentation.RecommendationsState
import com.g.pocketmal.ui.recommendations.presentation.RecommendedTitleViewEntity
import com.g.pocketmal.ui.theme.PocketMalTheme
import com.g.pocketmal.ui.utils.LazyLoadOnScrollListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

@AndroidEntryPoint
class RankedActivity : SkeletonActivity() {

    private val rankingType by transformedArgument<Int, RankingType>(
        EXTRA_TOP_TYPE,
        RankingType.ALL
    ) {
        RankingType.from(it)
    }
    private val titleType by transformedArgument<Int, TitleType>(
        EXTRA_TITLE_TYPE,
        TitleType.ANIME
    ) {
        TitleType.from(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                RankedContent(
                    rankingType = rankingType,
                    titleType = titleType,
                    onRankedItemClicked = { id ->
                        TitleDetailsActivity.start(this, id, titleType)
                    },
                    onBackPressed = { finish() },
                )
            }
        }
    }

    companion object {

        private const val EXTRA_TOP_TYPE = "extra_top_type"
        private const val EXTRA_TITLE_TYPE = "extra_title_type"

        private const val TITLES_IN_BUNCH = 50
        private const val LAZY_LOAD_OFFSET = 5

        fun start(context: Context, topType: RankingType, titleType: TitleType) {
            val intent = Intent(context, RankedActivity::class.java)
                .putExtra(EXTRA_TOP_TYPE, topType.type)
                .putExtra(EXTRA_TITLE_TYPE, titleType.type)
            context.startActivity(intent)
        }
    }
}

@Composable
private fun RankedContent(
    rankingType: RankingType,
    titleType: TitleType,
    viewModel: RankedViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onRankedItemClicked: (Int) -> Unit,
) {

    val rankedState by viewModel.rankedState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRankedTitles(rankingType, titleType)
    }

    RankedScreen(
        rankedState = rankedState,
        rankingType = rankingType,
        titleType = titleType,
        onRetryPressed = {
            viewModel.loadRankedTitles(rankingType, titleType)
        },
        onRankedItemClicked = onRankedItemClicked,
        onBackPressed = onBackPressed,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RankedScreen(
    rankedState: RankedState,
    rankingType: RankingType,
    titleType: TitleType,
    onRetryPressed: () -> Unit,
    onBackPressed: () -> Unit,
    onRankedItemClicked: (Int) -> Unit,
) {
    val labelId = when (rankingType) {
        RankingType.ALL ->
            if (titleType == TitleType.ANIME) R.string.topAnime else R.string.topManga

        RankingType.BY_POPULARITY ->
            if (titleType == TitleType.ANIME) R.string.mostPopularAnime else R.string.mostPopularManga

        else -> R.string.topAnime
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = labelId)) },
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
            when (rankedState) {
                is RankedState.RankedItems -> RankedList(
                    rankedItems = rankedState.items,
                    onRankedItemClicked = onRankedItemClicked,
                )

                RankedState.Loading -> LoadingView()

                is RankedState.Error ->
                    ErrorMessageWithRetryView(
                        message = rankedState.message,
                        onRetryClicked = onRetryPressed,
                    )

                RankedState.LoadingMore -> {

                }
            }
        }
    }
}

@Composable
private fun RankedList(
    rankedItems: List<RankedItemViewEntity>,
    onRankedItemClicked: (Int) -> Unit,
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
        items(rankedItems.size) { index ->
            RecommendationItem(
                rankedItem = rankedItems[index],
                onClick = onRankedItemClicked,
            )
        }
    }
}

@Composable
private fun RecommendationItem(
    rankedItem: RankedItemViewEntity,
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
        onClick = { onClick(rankedItem.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Poster(
                    url = rankedItem.poster,
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
                        text = rankedItem.title,
                        style = MaterialTheme.typography.titleMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = rankedItem.position,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    /*SmallDetailsRow(
                        score = rankedItem.score,
                        details = recommendation.details,
                    )*/
                }
            }
            /*val inMyListStatus = recommendation.inListStatus
            if (inMyListStatus.isInMyList) {
                InListStatusLabel(
                    status = inMyListStatus.statusLabel,
                    color = inMyListStatus.color,
                )
            }*/
        }
    }
}
