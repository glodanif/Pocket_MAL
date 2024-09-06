package com.g.pocketmal.ui.legacy.presenter

import androidx.lifecycle.LifecycleObserver
import com.g.pocketmal.domain.interactor.LogoutInteractor

open class BasePresenter(
    private val view: com.g.pocketmal.ui.legacy.view.BaseSessionView,
    private val route: com.g.pocketmal.ui.legacy.route.BaseSessionRoute,
    private val logoutInteractor: LogoutInteractor
) : LifecycleObserver {

    fun forceLogout() {

        logoutInteractor.execute(Unit, onComplete = {
            view.notifyUserAboutForceLogout()
            route.redirectToLoginScreen()
        })
    }
}
