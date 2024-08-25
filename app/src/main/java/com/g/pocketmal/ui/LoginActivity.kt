package com.g.pocketmal.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.g.pocketmal.ui.presenter.LoginPresenter
import com.g.pocketmal.ui.route.LoginRoute
import com.g.pocketmal.ui.view.LoginView
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class LoginActivity : SkeletonActivity(), LoginView, LoginRoute {

    private val authWebView: WebView by bind(R.id.wv_auth)
    private val loadingIndicator: ProgressBar by bind(R.id.pb_loading)
    private val noConnectionMessage: View by bind(R.id.ll_no_connection)

    private val presenter: LoginPresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authWebView.clearCache(true)
        authWebView.clearHistory()
        authWebView.settings.let {
            it.domStorageEnabled = true
            it.databaseEnabled = true
            it.javaScriptEnabled = true
            it.userAgentString = System.getProperty("http.agent")
        }

        authWebView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return presenter.checkUrlAndAuthorize(url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                presenter.onPageLoaded()
            }
        }

        findViewById<Button>(R.id.btn_refresh).setOnClickListener {
            presenter.loadWebView()
        }

        presenter.loadWebView()
    }

    override fun loadWebView(url: String) {
        authWebView.loadUrl(url)
    }

    override fun displayWebView() {
        authWebView.visibility = View.VISIBLE
        loadingIndicator.visibility = View.GONE
        noConnectionMessage.visibility = View.GONE
    }

    override fun displayNoInternet() {
        authWebView.visibility = View.GONE
        loadingIndicator.visibility = View.GONE
        noConnectionMessage.visibility = View.VISIBLE
    }

    override fun displayWebViewLoading() {
        authWebView.visibility = View.GONE
        loadingIndicator.visibility = View.VISIBLE
        noConnectionMessage.visibility = View.GONE
    }

    override fun displayLoginError() {
        showToast(R.string.login__login_failure)
    }

    override fun showProgress() {
        showProgressDialog(getString(R.string.login))
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun redirectToListScreen() {
        startActivity(Intent(this, ListActivity::class.java))
        finish()
    }

    override fun onBackPressed() {

        if (authWebView.canGoBack()) {
            authWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
