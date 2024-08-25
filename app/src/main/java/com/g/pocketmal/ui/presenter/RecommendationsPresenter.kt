package com.g.pocketmal.ui.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.data.api.SessionExpiredException
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.exception.EmptyResponseException
import com.g.pocketmal.domain.interactor.GetRecommendationsInteractor
import com.g.pocketmal.domain.interactor.LogoutInteractor
import com.g.pocketmal.ui.route.RecommendationsRoute
import com.g.pocketmal.ui.view.RecommendationsView
import com.g.pocketmal.ui.viewmodel.converter.RecommendedTitleConverter

class RecommendationsPresenter(
        private val id: Int,
        private val type: TitleType,
        private val view: RecommendationsView,
        private val route: RecommendationsRoute,
        private val converter: RecommendedTitleConverter,
        private val getRecommendationsInteractor: GetRecommendationsInteractor,
        private val logoutInteractor: LogoutInteractor
) : BasePresenter(view, route, logoutInteractor) {

    fun loadRecommendations() {

        view.showProgress()

        getRecommendationsInteractor.execute(GetRecommendationsInteractor.Params(id, type),
                onResult = { recommendations ->
                    val viewModels = converter.transform(recommendations, type)
                    view.displayRecommendations(viewModels)
                },
                onError = { error ->
                    when (error) {
                        is SessionExpiredException -> {
                            forceLogout()
                        }
                        is EmptyResponseException -> {
                            view.displayNoRecommendations()
                        }
                        else -> {
                            view.displayError()
                        }
                    }
                },
                onComplete = {
                    view.hideProgress()
                }
        )
    }

    fun itemClick(id: Int) {
        route.openDetailsScreen(id, type)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        getRecommendationsInteractor.cancel()
    }
}
