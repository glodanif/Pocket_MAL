package com.g.pocketmal.ui.poster

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.toggleScale
import net.engawapg.lib.zoomable.zoomable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PosterScreen(
    posterUrl: String,
    onDownloadClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Poster") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onShareClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share image button",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { onDownloadClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = "Download image button",
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
            if (posterUrl.isEmpty()) {
                InvalidPosterUrlView()
            } else {
                PosterView(posterUrl)
            }
        }
    }
}

@Composable
private fun PosterView(posterUrl: String) {

    val zoomState = rememberZoomState()

    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .zoomable(
                zoomState = zoomState,
                onDoubleTap = { position ->
                    zoomState.toggleScale(2.5f, position)
                }
            ),
        model = posterUrl,
        contentDescription = "",
        contentScale = ContentScale.Fit,
        onSuccess = { state ->
            zoomState.setContentSize(state.painter.intrinsicSize)
        },
    )
}

@Composable
private fun InvalidPosterUrlView() {
    Box(
        modifier = Modifier.padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Unable to get the valid poster URL, please relaunch the app...",
            textAlign = TextAlign.Center,
        )
    }
}
