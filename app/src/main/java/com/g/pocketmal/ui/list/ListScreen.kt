package com.g.pocketmal.ui.list

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.g.pocketmal.R
import com.g.pocketmal.data.common.ListCounts
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.common.ErrorMessageWithRetryView
import com.g.pocketmal.ui.common.LoadingView
import com.g.pocketmal.ui.common.Poster

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    titleType: TitleType,
    onRecordClicked: (Int, TitleType) -> Unit,
) {

    val listState by viewModel.listState.collectAsState()
    val state = listState

    var isListStatusSelectorOpened by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        if (state == ListState.Initial) {
            viewModel.watchRecords(titleType)
            viewModel.synchronizeList(titleType)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = if (titleType.isAnime()) "Anime List" else "Manga List")
                        if (state is ListState.RecordsList) {
                            Text(
                                text = stringResource(id = state.statusLabel),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
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

            if (state is ListState.RecordsList) {
                RecordsContent(
                    state = state,
                    onRecordClicked = { id ->

                    },
                    onPulledToRefresh = {
                        viewModel.synchronizeList(titleType)
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                if (titleType == TitleType.ANIME) {
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(MaterialTheme.colorScheme.inversePrimary)
                            .clickable {
                                isListStatusSelectorOpened = true
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Rounded.KeyboardDoubleArrowUp,
                            contentDescription = "Anime status selector icon",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                } else {
                    Box(modifier = Modifier.width(72.dp))
                }
                if (titleType == TitleType.MANGA) {
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(MaterialTheme.colorScheme.inversePrimary)
                            .clickable {
                                isListStatusSelectorOpened = true
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Rounded.KeyboardDoubleArrowUp,
                            contentDescription = "Manga status selector icon",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                } else {
                    Box(modifier = Modifier.width(72.dp))
                }
                Box(modifier = Modifier.width(72.dp))
                Box(modifier = Modifier.width(72.dp))
                Box(modifier = Modifier.width(72.dp))
            }
        }
    }

    if (isListStatusSelectorOpened && state is ListState.RecordsList) {
        StatusSelector(
            titleType = titleType,
            selectedStatus = state.status,
            counts = state.counts,
            onNewStatusSelected = { status ->
                isListStatusSelectorOpened = false
                viewModel.switchStatus(titleType, status)
            },
            onDismissRequest = {
                isListStatusSelectorOpened = false
            }
        )
    }

    if (state is ListState.RecordsList) {
        BackHandler(enabled = state.status != Status.IN_PROGRESS) {
            viewModel.switchStatus(titleType, Status.IN_PROGRESS)
        }
    }
}

@Composable
private fun RecordsContent(
    state: ListState.RecordsList,
    onPulledToRefresh: () -> Unit,
    onRecordClicked: (Int) -> Unit
) {

    if (!state.isPreloaded) {
        if (!state.isSynchronized && state.synchronizationError != null) {
            ErrorMessageWithRetryView(
                message = "EROROR ${state.synchronizationError}",
                onRetryClicked = { })
        } else {
            LoadingView()
        }
    } else if (!state.isSynchronized && state.synchronizationError != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                "Last sync: ${state.synchronizedAt}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
            RecordsListContainer(
                state = state,
                onRecordClicked = onRecordClicked,
                onPulledToRefresh = onPulledToRefresh,
            )
        }
        val context = LocalContext.current
        LaunchedEffect(state.synchronizationError) {
            Toast.makeText(context, state.synchronizationError, Toast.LENGTH_LONG).show()
        }
    } else {
        RecordsListContainer(
            state = state,
            onRecordClicked = onRecordClicked,
            onPulledToRefresh = onPulledToRefresh,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordsListContainer(
    state: ListState.RecordsList,
    onPulledToRefresh: () -> Unit,
    onRecordClicked: (Int) -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(state) {
        isRefreshing = state.isSynchronizing
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            onPulledToRefresh()
        },
    ) {
        if (state.list.isNotEmpty()) {
            RecordsList(
                records = state.list,
                onRecordClicked = onRecordClicked,
            )
        } else {
            EmptyList(stringResource(id = state.statusLabel))
        }
    }
}

@Composable
private fun RecordsList(
    records: List<RecordListViewEntity>,
    onRecordClicked: (Int) -> Unit,
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
        items(records.size) { index ->
            RecordItem(
                record = records[index],
                onClick = onRecordClicked,
            )
        }
    }
}

@Composable
private fun EmptyList(
    statusLabel: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            modifier = Modifier.size(96.dp),
            model = R.drawable.wind,
            contentDescription = "Wind illustration",
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.someListIsEmpty, statusLabel),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
private fun RecordItem(
    record: RecordListViewEntity,
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
        onClick = { onClick(record.seriesId) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Poster(
                    url = record.seriesPosterUrl,
                    modifier = Modifier
                        .height(150.dp)
                        .width(107.dp),
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = record.seriesTitle,
                        style = MaterialTheme.typography.titleMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = record.seriesDetails,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusSelector(
    titleType: TitleType,
    selectedStatus: Status,
    counts: ListCounts,
    onNewStatusSelected: (Status) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val statusSelectorItems = listOf(
        StatusSelectorItem(
            label = if (titleType.isAnime()) "Watching" else "Reading",
            status = Status.IN_PROGRESS,
            count = counts.inProgressCount.toString(),
            isSelected = selectedStatus == Status.IN_PROGRESS,
        ),
        StatusSelectorItem(
            label = "Completed",
            count = counts.completedCount.toString(),
            status = Status.COMPLETED,
            isSelected = selectedStatus == Status.COMPLETED,
        ),
        StatusSelectorItem(
            label = "On hold",
            count = counts.onHoldCount.toString(),
            status = Status.ON_HOLD,
            isSelected = selectedStatus == Status.ON_HOLD,
        ),
        StatusSelectorItem(
            label = "Dropped",
            count = counts.droppedCount.toString(),
            status = Status.DROPPED,
            isSelected = selectedStatus == Status.DROPPED,
        ),
        StatusSelectorItem(
            label = if (titleType.isAnime()) "Plan to watch" else "Plan to read",
            count = counts.plannedCount.toString(),
            status = Status.PLANNED,
            isSelected = selectedStatus == Status.PLANNED,
        ),
    )
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp, top = 24.dp, start = 36.dp, end = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Your Anime List",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(36.dp))
            statusSelectorItems.forEach { item ->
                if (item.isSelected) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {},
                    ) {
                        StatusButtonContent(item.label, item.count)
                    }
                } else {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onNewStatusSelected(item.status)
                        },
                    ) {
                        StatusButtonContent(item.label, item.count)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            StatusButtonContent(
                label = "Total",
                value = counts.generalCount.toString(),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatusButtonContent(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(modifier = Modifier.width(54.dp))
        Text(text = label)
        Text(
            text = value,
            modifier = Modifier.width(54.dp),
            textAlign = TextAlign.End,
        )
    }
}

data class StatusSelectorItem(
    val label: String,
    val count: String,
    val status: Status,
    val isSelected: Boolean,
)
