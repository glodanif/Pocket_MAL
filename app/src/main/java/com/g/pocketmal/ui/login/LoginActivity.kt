package com.g.pocketmal.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.g.pocketmal.ui.common.ErrorMessageWithRetryView
import com.g.pocketmal.ui.common.LoadingView
import com.g.pocketmal.ui.legacy.ListActivity
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.login.presentation.LoginState
import com.g.pocketmal.ui.login.presentation.LoginViewModel
import com.g.pocketmal.ui.theme.PocketMalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : SkeletonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                LoginContent(
                    onAuthorized = {
                        startActivity(Intent(this, ListActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun LoginContent(
    viewModel: LoginViewModel = hiltViewModel(),
    onAuthorized: () -> Unit,
) {

    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()

    val webView = remember {
        WebView(context).apply {
            clearCache(true)
            clearHistory()
            settings.let {
                it.domStorageEnabled = true
                it.databaseEnabled = true
                it.javaScriptEnabled = true
                it.userAgentString = System.getProperty("http.agent")
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    val code = viewModel.findCode(request.url.toString())
                    if (code != null) {
                        viewModel.authorize(code)
                        return true
                    } else {
                        return false
                    }
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    viewModel.handleAuthPageLoaded()
                }
            }
        }
    }

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Authorized) {
            onAuthorized()
        }
    }

    when (val state = loginState) {
        is LoginState.AuthDataReady -> AuthPageLoading(webView, state.url)
        LoginState.AuthPageReady -> LoginScreen(webView)
        LoginState.Authorized -> LoadingView()
        LoginState.Authorizing -> LoadingView()
        is LoginState.Error -> ErrorMessageWithRetryView(
            message = "Error",
            onRetryClicked = { viewModel.loadAuthPageUrlData() },
        )

        LoginState.LoadingWebPage -> LoadingView()
        LoginState.NoInternet -> ErrorMessageWithRetryView(
            message = "No Internet",
            onRetryClicked = { viewModel.loadAuthPageUrlData() }
        )
    }

    BackHandler {
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }
}

@Composable
fun AuthPageLoading(
    webView: WebView,
    url: String,
) {
    LaunchedEffect(url) {
        webView.loadUrl(url)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize())
    }
}


@Composable
fun LoginScreen(
    webView: WebView,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize())
    }
}

