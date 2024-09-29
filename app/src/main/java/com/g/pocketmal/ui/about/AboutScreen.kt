package com.g.pocketmal.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.g.pocketmal.BuildConfig
import com.g.pocketmal.R

const val APPLICATION_LINK = "https://play.google.com/store/apps/details?id=com.g.pocketmal"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onRateClicked: () -> Unit,
    onShareClicked: (String) -> Unit,
    onOpenSourceLibrariesClicked: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
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
                    IconButton(onClick = { onRateClicked() }) {
                        Icon(
                            imageVector = Icons.Rounded.StarRate,
                            contentDescription = "Rate app button",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { onShareClicked(APPLICATION_LINK) }) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share app button",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
    ) { innerPaddings ->
        Column(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            AsyncImage(
                modifier = Modifier.size(96.dp),
                model = R.mipmap.ic_launcher,
                contentDescription = "Pocket mal logo",
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Pocket MAL\nv${BuildConfig.VERSION_NAME} / ${BuildConfig.VERSION_CODE}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onOpenSourceLibrariesClicked()
                }) {
                Text(text = "Open Source libraries")
            }
        }
    }
}
