package com.g.pocketmal.ui.poster

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.compose.setContent
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
import com.g.pocketmal.R
import com.g.pocketmal.argument
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.theme.PocketMalTheme
import dagger.hilt.android.AndroidEntryPoint
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.toggleScale
import net.engawapg.lib.zoomable.zoomable

@AndroidEntryPoint
class PosterActivity : SkeletonActivity() {

    private val posterUrl by argument<String?>(EXTRA_POSTER_URL)

    private val downloadingReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            showToast(R.string.poster__downloading_completed)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                PosterContent(
                    posterUrl = posterUrl,
                    onBackPressed = { finish() },
                    onDownloadClicked = { downloadPoster() },
                    onShareClicked = { sharePoster() }
                )
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                downloadingReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), RECEIVER_EXPORTED
            )
        } else {
            registerReceiver(
                downloadingReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(downloadingReceiver)
    }

    private fun sharePoster() {
        posterUrl?.let { url ->
            val isValidUrl = url.startsWith("http", false)
            if (url.isEmpty() || !isValidUrl) {
                return
            }
            shareText(url)
        }
    }

    private fun downloadPoster() {

        val isValidUrl = posterUrl?.startsWith("http", false) ?: false
        if (posterUrl.isNullOrEmpty() || !isValidUrl) {
            return
        }

        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        val downloadsDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDirectory.exists()) {
            downloadsDirectory.mkdirs()
        }

        val posterUri = Uri.parse(posterUrl)
        manager.enqueue(
            DownloadManager.Request(posterUri)
                .setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
                )
                .setAllowedOverRoaming(false)
                .setTitle(getString(R.string.poster__downloading_title))
                .setDescription(getString(R.string.poster__downloading_description))
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    posterUri.lastPathSegment
                )
        )
    }

    companion object {

        private const val EXTRA_POSTER_URL = "extra.poster_url"

        fun start(context: Context, url: String?) {
            val intent = Intent(context, PosterActivity::class.java).apply {
                putExtra(EXTRA_POSTER_URL, url)
            }
            context.startActivity(intent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PosterContent(
    posterUrl: String?,
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
            if (posterUrl.isNullOrEmpty()) {
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
