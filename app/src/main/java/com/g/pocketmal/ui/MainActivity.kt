package com.g.pocketmal.ui

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.g.pocketmal.R
import com.g.pocketmal.ui.home.HomeScreen
import com.g.pocketmal.ui.login.LoginScreen
import com.g.pocketmal.ui.splash.DispatcherScreen
import com.g.pocketmal.ui.theme.PocketMalTheme
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                PocketMalApp(
                    rateApp = {
                        promptAppReview()
                    },
                    shareText = { text ->
                        shareText(text)
                    },
                    openLink = { link ->

                    },
                    copyToClipboard = { text ->
                        copyToClipboard(text)
                    },
                    downloadFile = { url ->
                        downloadFile(url)
                    }
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(downloadingReceiver)
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
                Toast.makeText(
                    this,
                    "Unable to launch the review process ($reviewErrorCode)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun shareText(testToShare: String) {
        try {
            val shareLinkIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, testToShare)
                putExtra(Intent.EXTRA_TEXT, testToShare)
            }
            startActivity(
                Intent.createChooser(
                    shareLinkIntent,
                    getString(R.string.general__share_via)
                )
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.noShareApp, Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(this, R.string.noShareApp, Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyToClipboard(text: String) {
        (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText(text, text))
    }

    private val downloadingReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            unregisterReceiver(this)
            Toast.makeText(context, R.string.poster__downloading_completed, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun downloadFile(url: String) {

        val isValidUrl = url.startsWith("http", false)
        if (url.isEmpty() || !isValidUrl) {
            return
        }

        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        val downloadsDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDirectory.exists()) {
            downloadsDirectory.mkdirs()
        }

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

        val posterUri = Uri.parse(url)
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
}

@Serializable
data object Dispatcher

@Serializable
data object Login

@Serializable
data object Home

@Composable
private fun PocketMalApp(
    rateApp: () -> Unit,
    shareText: (String) -> Unit,
    openLink: (String) -> Unit,
    copyToClipboard: (String) -> Unit,
    downloadFile: (String) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Dispatcher) {
        composable<Dispatcher> {
            DispatcherScreen(
                onListLoaded = {
                    navController.navigate(Home) {
                        popUpTo(Dispatcher) { inclusive = true }
                    }
                },
                onNotLoggedIn = {
                    navController.navigate(Login) {
                        popUpTo(Dispatcher) { inclusive = true }
                    }
                },
            )
        }
        composable<Login> {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }
        composable<Home> {
            HomeScreen(
                rateApp = rateApp,
                shareText = shareText,
                openLink = openLink,
                copyToClipboard = copyToClipboard,
            )
        }
    }
}
