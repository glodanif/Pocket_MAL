package com.g.pocketmal.ui.search

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.transformedArgument
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.legacy.TitleDetailsActivity
import com.g.pocketmal.ui.search.presentation.SearchResultViewEntity
import com.g.pocketmal.ui.search.presentation.SearchState
import com.g.pocketmal.ui.search.presentation.SearchViewModel
import com.g.pocketmal.ui.theme.PocketMalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : SkeletonActivity() {

    private val type by transformedArgument<Int, TitleType>(EXTRA_SEARCH_TYPE, TitleType.ANIME) {
        TitleType.from(it)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                SearchContent(
                    titleType = type,
                    onSearchItemClick = { id ->
                        TitleDetailsActivity.start(this, id, type)
                    },
                    onBackPressed = { finish() },
                )
            }
        }
    }

    companion object {

        private const val EXTRA_SEARCH_TYPE = "extra.search_type"

        fun start(context: Context, type: TitleType) {
            val intent = Intent(context, SearchActivity::class.java).apply {
                putExtra(EXTRA_SEARCH_TYPE, type.type)
            }
            context.startActivity(intent)
        }
    }
}

@Composable
private fun SearchContent(
    titleType: TitleType,
    viewModel: SearchViewModel = hiltViewModel(),
    onSearchItemClick: (Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    val searchState by viewModel.searchState.collectAsState()
    SearchScreen(
        titleType = titleType,
        searchState = searchState,
        onBackPressed = onBackPressed,
        onSearchPressed = { query ->
            viewModel.search(query, titleType)
        },
        onSearchItemClick = onSearchItemClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    titleType: TitleType,
    searchState: SearchState,
    onSearchPressed: (String) -> Unit,
    onBackPressed: () -> Unit,
    onSearchItemClick: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = if (titleType == TitleType.ANIME) "Anime Search" else "Manga Search"
                    Text(title)
                },
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
        var query: String by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current

        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                when (searchState) {
                    is SearchState.SearchResult -> {
                        SearchResultList(
                            searchResults = searchState.result,
                            onItemClicked = onSearchItemClick,
                        )
                    }

                    is SearchState.FailedToLoad -> {
                        FailedToLoadView(
                            failedState = searchState,
                            onRetryPressed = {
                                onSearchPressed(query)
                            },
                        )
                    }

                    is SearchState.IncorrectInput -> IncorrectInputView(searchState.minQueryLength)
                    SearchState.Initial -> Box {}
                    SearchState.Loading -> LoadingView()
                    SearchState.NoSearchResult -> EmptySearchResultView()
                }
            }
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)
                    .semantics { traversalIndex = 0f },
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = {
                            keyboardController?.hide()
                            onSearchPressed(it.trim())
                        },
                        expanded = false,
                        onExpandedChange = { },
                        placeholder = { Text("Search query") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = null,
                            )
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(
                                    onClick = { query = "" },
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        contentDescription = "Clear search field button",
                                    )
                                }
                            }
                        }
                    )
                },
                expanded = false,
                onExpandedChange = { },
                shadowElevation = 12.dp,
            ) {}
        }
    }
}

@Composable
private fun SearchResultList(
    searchResults: List<SearchResultViewEntity>,
    onItemClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 116.dp)
    ) {
        items(searchResults.size) { index ->
            SearchResultItem(
                searchResultItem = searchResults[index],
                onClick = onItemClicked,
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    searchResultItem: SearchResultViewEntity,
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
        onClick = { onClick(searchResultItem.id) }
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(88.dp),
                model = searchResultItem.poster,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            ) {
                Text(
                    text = searchResultItem.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
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
private fun IncorrectInputView(minQueryLength: Int) {
    Box(
        modifier = Modifier.padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Query must be at least $minQueryLength characters long",
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EmptySearchResultView() {
    Box(
        modifier = Modifier.padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "No recommendations for this title", textAlign = TextAlign.Center)
    }
}

@Composable
private fun FailedToLoadView(
    failedState: SearchState.FailedToLoad,
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
