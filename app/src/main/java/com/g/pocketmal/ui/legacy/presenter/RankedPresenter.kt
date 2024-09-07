package com.g.pocketmal.ui.legacy.presenter

import com.g.pocketmal.R
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.interactor.GetTopInteractor
import com.g.pocketmal.domain.interactor.LogoutInteractor
import com.g.pocketmal.ui.legacy.viewentity.converter.RankedItemConverter

class RankedPresenter(
    private val rankingType: RankingType,
    private val titleType: TitleType,
    private val view: com.g.pocketmal.ui.legacy.view.RankedView,
    private val route: com.g.pocketmal.ui.legacy.route.RankedRoute,
    private val converter: RankedItemConverter,
    private val getTopInteractor: GetTopInteractor,
    private val logoutInteractor: LogoutInteractor
) : BasePresenter(view, route, logoutInteractor) {

    fun attach() {

        val labelId = when (rankingType) {
            RankingType.ALL -> if (titleType == TitleType.ANIME) R.string.topAnime else R.string.topManga
            RankingType.BY_POPULARITY -> if (titleType == TitleType.ANIME) R.string.mostPopularAnime else R.string.mostPopularManga
            else -> R.string.topAnime
        }
        view.setToolbarTitle(labelId)
    }

    fun loadTopWithOffset(offset: Int) {

        view.showProgressFooter()

        getTopInteractor.execute(GetTopInteractor.Params(titleType, rankingType, offset),
                onResult = {list ->
                    view.addItemsToList(converter.transform(list, titleType))
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
