package com.g.pocketmal.ui.presenter

import androidx.lifecycle.LifecycleObserver
import com.g.pocketmal.domain.interactor.LogoutInteractor
import com.g.pocketmal.ui.route.BaseSessionRoute
import com.g.pocketmal.ui.view.BaseSessionView

open class BasePresenter(
        private val view: BaseSessionView,
        private val route: BaseSessionRoute,
        private val logoutInteractor: LogoutInteractor
) : LifecycleObserver {

    fun forceLogout() {

        logoutInteractor.execute(Unit, onComplete = {
            view.notifyUserAboutForceLogout()
            route.redirectToLoginScreen()
        })
    }
}
