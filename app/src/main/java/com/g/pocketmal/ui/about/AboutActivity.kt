package com.g.pocketmal.ui.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
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
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.theme.PocketMalTheme
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutActivity : SkeletonActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                AboutContent(
                    onShareClicked = { shareText(APPLICATION_LINK) },
                    onRateClicked = { promptAppReview() },
                    onOpenSourceLibrariesClicked = {
                        startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                    },
                    onBackPressed = { finish() },
                )
            }
        }
    }

    private fun promptAppReview() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                manager.launchReviewFlow(this, task.result)
            } else {
                @ReviewErrorCode
                val reviewErrorCode = (task.exception as ReviewException).errorCode
                showToast("Unable to launch the review process ($reviewErrorCode)")
            }
        }
    }

    companion object {
        const val APPLICATION_LINK = "https://play.google.com/store/apps/details?id=com.g.pocketmal"

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutContent(
    onRateClicked: () -> Unit,
    onShareClicked: () -> Unit,
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
                    IconButton(onClick = { onShareClicked() }) {
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
