package com.g.pocketmal.ui.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.data.api.SessionExpiredException
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.exception.EmptyResponseException
import com.g.pocketmal.domain.interactor.LogoutInteractor
import com.g.pocketmal.domain.interactor.SearchInteractor
import com.g.pocketmal.ui.route.SearchRoute
import com.g.pocketmal.ui.view.SearchView
import com.g.pocketmal.ui.viewmodel.converter.SearchResultConverter
import java.lang.IllegalArgumentException

class SearchPresenter(
        private val titleType: TitleType,
        private val view: SearchView,
        private val route: SearchRoute,
        private val searchInteractor: SearchInteractor,
        private val converter: SearchResultConverter,
        private val logoutInteractor: LogoutInteractor
) : BasePresenter(view, route, logoutInteractor) {

    fun search(query: String) {

        view.showProgress()
        view.hideFailPopup()

        searchInteractor.execute(SearchInteractor.Params(query, titleType),
                onResult = { list ->
                    view.displaySearchResult(converter.transform(list, titleType))
                },
                onError = { error ->
                    when (error) {
                        is SessionExpiredException -> {
                            forceLogout()
                        }
                        is IllegalArgumentException -> {
                            view.showQueryIsTooShortMessage()
                        }
                        is EmptyResponseException -> {
                            view.displayEmptyResult()
                        }
                        else -> {
                            view.showFailPopup()
                        }
                    }
                },
                onComplete = {
                    view.hideProgress()
                }
        )
    }

    fun openDetails(id: Int) {
        route.openDetailsScreen(id, titleType)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        searchInteractor.cancel()
    }
}
