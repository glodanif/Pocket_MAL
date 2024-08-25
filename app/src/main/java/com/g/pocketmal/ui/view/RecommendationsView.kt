package com.g.pocketmal.ui.view

import com.g.pocketmal.ui.viewmodel.RecommendedTitleViewModel

interface RecommendationsView: BaseSessionView {
    fun displayRecommendations(items: List<RecommendedTitleViewModel>)
    fun displayNoRecommendations()
    fun displayError()
    fun showProgress()
    fun hideProgress()
}