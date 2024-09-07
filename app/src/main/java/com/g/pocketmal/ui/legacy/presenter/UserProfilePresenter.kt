package com.g.pocketmal.ui.legacy.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.data.api.SessionExpiredException
import com.g.pocketmal.domain.interactor.GetUserProfileInteractor
import com.g.pocketmal.domain.interactor.LogoutInteractor
import com.g.pocketmal.ui.legacy.viewentity.converter.UserProfileConverter

class UserProfilePresenter(
    private val userId: Int,
    private val view: com.g.pocketmal.ui.legacy.view.UserProfileView,
    private val route: com.g.pocketmal.ui.legacy.route.UserProfileRoute,
    private val getUserProfileInteractor: GetUserProfileInteractor,
    private val converter: UserProfileConverter,
    private val logoutInteractor: LogoutInteractor
) : BasePresenter(view, route, logoutInteractor) {

    private var viewModel: com.g.pocketmal.ui.legacy.viewentity.UserProfileViewModel? = null

    fun loadUserProfile() {

        getUserProfileInteractor.execute(userId,
                onCacheResult = { entity ->
                    viewModel = converter.transform(entity)
                    viewModel?.let { view.displayUserInfo(it) }
                },
                onCacheError = {
                    view.showProgress()
                },
                onNetworkResult = { entity ->
                    viewModel = converter.transform(entity)
                    viewModel?.let { view.displayUserInfo(it) }
                },
                onNetworkError = { throwable ->
                    if (throwable is SessionExpiredException) {
                        forceLogout()
                    } else {
                        view.showFailNotification()
                    }
                },
                onComplete = {
                    view.hideProgress()
                }
        )
    }

    fun imageClick() {
        route.openUserImage(viewModel?.avatar ?: "")
    }

    fun viewOnMalClick() {
        route.openOnMal(viewModel?.malLink ?: "")
    }

    fun logout() {

        logoutInteractor.execute(Unit,
                onResult = {
                    route.redirectToLoginScreen()
                },
                onError = { throw it }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        getUserProfileInteractor.cancel()
    }
}
