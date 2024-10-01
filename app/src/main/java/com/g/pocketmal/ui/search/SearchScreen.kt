package com.g.pocketmal.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
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
import com.g.pocketmal.ui.common.innerShadow
import com.g.pocketmal.ui.search.presentation.SearchResultViewEntity
import com.g.pocketmal.ui.search.presentation.SearchState
import com.g.pocketmal.ui.search.presentation.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onSearchItemClick: (Int, TitleType) -> Unit,
) {
    val searchState by viewModel.searchState.collectAsState()
    var titleType by remember { mutableStateOf(TitleType.ANIME) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPaddings ->
        var query: String by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current

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
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                when (val state = searchState) {
                    is SearchState.SearchResult -> {
                        SearchResultList(
                            searchResults = state.result,
                            onItemClicked = { id -> onSearchItemClick(id, titleType) },
                        )
                    }

                    is SearchState.FailedToLoad ->
                        ErrorMessageWithRetryView(
                            message = state.errorMessage,
                            onRetryClicked = { viewModel.search(query, titleType) },
                        )

                    is SearchState.IncorrectInput ->
                        ErrorMessageView(
                            message = "Query must be at least ${state.minQueryLength} characters long",
                        )

                    SearchState.NoSearchResult ->
                        ErrorMessageView(
                            message = "No result for this query...",
                        )

                    SearchState.Initial -> Box {}
                    SearchState.Loading -> LoadingView()

                }
            }
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .semantics { traversalIndex = 0f },
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = {
                            keyboardController?.hide()
                            viewModel.search(it.trim(), titleType)
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                TitleTypeSwitch(
                                    titleType = titleType,
                                    onTypeChanged = { type ->
                                        titleType = type
                                    },
                                )
                                Spacer(modifier = Modifier.width(12.dp))
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
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 108.dp)
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
            .shadow(
                elevation = 8.dp,
                shape = CardDefaults.shape,
                spotColor = Color.Black
            ),
        onClick = { onClick(searchResultItem.id) }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Poster(
                    modifier = Modifier
                        .height(150.dp)
                        .width(103.dp),
                    url = searchResultItem.poster,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = searchResultItem.title,
                        style = MaterialTheme.typography.titleMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SmallDetailsRow(
                        score = searchResultItem.score,
                        details = searchResultItem.details,
                    )
                }
            }
            val inMyListStatus = searchResultItem.inListStatus
            if (inMyListStatus.isInMyList) {
                InListStatusLabel(
                    status = inMyListStatus.statusLabel,
                    color = inMyListStatus.color,
                )
            }
            if (searchResultItem.synopsis.isNotEmpty()) {
                if (!inMyListStatus.isInMyList) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outline),
                    )
                }
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = searchResultItem.synopsis,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun TitleTypeSwitch(
    titleType: TitleType,
    onTypeChanged: (TitleType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(64.dp, 36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable {
                onTypeChanged(
                    if (titleType.isAnime()) TitleType.MANGA else TitleType.ANIME
                )
            }
    ) {
        if (titleType.isAnime()) {
            Box(
                modifier = Modifier
                    .size(64.dp, 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shadow(elevation = 4.dp)
                    .background(MaterialTheme.colorScheme.primary),
            )
            Box(
                modifier = Modifier
                    .size(64.dp, 56.dp)
                    .innerShadow(
                        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                        color = Color.Black.copy(alpha = .75f),
                    )
                    .padding(bottom = 2.dp)
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = "ANIME",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(64.dp, 56.dp)
                    .innerShadow(
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                        color = Color.Black.copy(alpha = .75f),
                    )
                    .padding(top = 2.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                Text(
                    text = "MANGA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
            Box(
                modifier = Modifier
                    .size(64.dp, 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shadow(elevation = 4.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}
