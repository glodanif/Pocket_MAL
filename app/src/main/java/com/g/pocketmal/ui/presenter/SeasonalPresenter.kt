package com.g.pocketmal.ui.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.data.api.SessionExpiredException
import com.g.pocketmal.data.util.PartOfYear
import com.g.pocketmal.data.util.Season
import com.g.pocketmal.domain.exception.EmptyResponseException
import com.g.pocketmal.domain.interactor.GetSeasonalAnimeInteractor
import com.g.pocketmal.domain.interactor.LogoutInteractor
import com.g.pocketmal.ui.route.SeasonalRoute
import com.g.pocketmal.ui.view.SeasonalView
import com.g.pocketmal.ui.viewmodel.converter.SeasonalSectionConverter
import com.g.pocketmal.util.AnimeSeason
import java.util.*

class SeasonalPresenter(
        private val view: SeasonalView,
        private val route: SeasonalRoute,
        private val getSeasonalAnimeInteractor: GetSeasonalAnimeInteractor,
        private val converter: SeasonalSectionConverter,
        private val logoutInteractor: LogoutInteractor
) : BasePresenter(view, route, logoutInteractor) {

    private val calendar = Calendar.getInstance()

    private var season = Season(
            AnimeSeason.parseSeason(calendar.get(Calendar.MONTH)),
            calendar.get(Calendar.YEAR)
    )

    private var isLoading = false

    fun loadLatestSeason() {
        loadSeason(season.year, season.partOfYear)
    }

    fun loadSeason(year: Int, partOfYear: PartOfYear) {

        if (isLoading) {
            view.askToWait()
            return
        }

        season = Season(partOfYear, year)

        view.displaySeason(year, partOfYear)

        isLoading = true
        view.showProgress()
        view.hideFailPopup()

        getSeasonalAnimeInteractor.execute(season,
                onResult = { list ->
                    view.displaySeasonalAnime(converter.transform(list, season))
                },
                onError = { error ->
                    when (error) {
                        is SessionExpiredException -> {
                            forceLogout()
                        }
                        is EmptyResponseException -> {
                            view.showEmptySeason()
                        }
                        else -> {
                            view.displayFailPopup()
                        }
                    }
                },
                onComplete = {
                    isLoading = false
                    view.hideProgress()
                }
        )
    }

    fun pickAnotherSeason() {
        route.openSeasonPicker(season)
    }

    fun itemClick(id: Int) {
        route.openDetailsScreen(id)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        getSeasonalAnimeInteractor.cancel()
    }
}
