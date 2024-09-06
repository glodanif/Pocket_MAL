package com.g.pocketmal.ui.legacy.presenter

import com.g.pocketmal.R
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.interactor.GetTopInteractor
import com.g.pocketmal.domain.interactor.LogoutInteractor

class BrowsePresenter(
    private val rankingType: RankingType,
    private val titleType: TitleType,
    private val view: com.g.pocketmal.ui.legacy.view.BrowseView,
    private val route: com.g.pocketmal.ui.legacy.route.BrowseRoute,
    private val converter: com.g.pocketmal.ui.legacy.viewmodel.converter.BrowseItemConverter,
    private val getTopInteractor: GetTopInteractor,
    private val logoutInteractor: LogoutInteractor
) : BasePresenter(view, route, logoutInteractor) {

    fun attach() {

        val labelId = when (rankingType) {
            RankingType.UPCOMING -> R.string.upcomingAnime
            RankingType.AIRING -> R.string.airingAnime
            else -> R.string.upcomingAnime
        }
        view.setToolbarTitle(labelId)
    }

    fun loadTopWithOffset(offset: Int) {

        view.showProgressFooter()

        getTopInteractor.execute(GetTopInteractor.Params(titleType, rankingType, offset),
                onResult = {list ->
                    view.addItemsToList(converter.transform(list))
                },
                onError = {
                    view.notifyLoadingFailure()
                    view.displayFailPopup()
                },
                onComplete = {
                    view.hideProgressFooter()
                }
        )
    }

    fun itemClick(id: Long) {
        route.openDetailsScreen(id.toInt(), titleType)
    }
}
