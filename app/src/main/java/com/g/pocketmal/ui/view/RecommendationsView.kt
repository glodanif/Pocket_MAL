package com.g.pocketmal.ui.view

import com.g.pocketmal.ui.recommendations.RecommendedTitleViewEntity

interface RecommendationsView: BaseSessionView {
    fun displayRecommendations(items: List<RecommendedTitleViewEntity>)
    fun displayNoRecommendations()
    fun displayError()
    fun showProgress()
    fun hideProgress()
}