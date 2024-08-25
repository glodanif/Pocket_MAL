package com.g.pocketmal.ui.presenter

import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.domain.interactor.AuthorizationInteractor
import com.g.pocketmal.domain.interactor.LoadLoginPageInteractor
import com.g.pocketmal.ui.route.LoginRoute
import com.g.pocketmal.ui.view.LoginView
import java.io.IOException

class LoginPresenter(
        private val view: LoginView,
        private val route: LoginRoute,
        private val authorizationInteractor: AuthorizationInteractor,
        private val loadLoginPageInteractor: LoadLoginPageInteractor
) : LifecycleObserver {

    private var codeVerifier: String = ""
    private var redirectUrl: String = ""

    fun loadWebView() {

        view.displayWebViewLoading()

        loadLoginPageInteractor.execute(Unit,
                onResult = { auth ->
                    codeVerifier = auth.codeVerifier
                    redirectUrl = auth.redirectUrl
                    view.loadWebView(auth.url)
                },
                onError = { error ->
                    if (error is IOException) {
                        view.displayNoInternet()
                    } else {
                        throw error
                    }
                }
        )
    }

    fun onPageLoaded() {
        view.displayWebView()
    }

    fun checkUrlAndAuthorize(url: String): Boolean {

        val code: String?
        try {
            code = Uri.parse(url).getQueryParameter("code")
            if (!url.contains(redirectUrl) || code == null || code.isEmpty()) {
                return false
            }
        } catch (e: UnsupportedOperationException) {
            return false
        }

        view.showProgress()

        authorizationInteractor.execute(AuthorizationInteractor.Params(code, codeVerifier),
                onResult = {
                    route.redirectToListScreen()
                },
                onError = {
                    view.displayLoginError()
                },
                onComplete = {
                    view.hideProgress()
                }
        )

        return true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        authorizationInteractor.cancel()
        loadLoginPageInteractor.cancel()
    }
}
