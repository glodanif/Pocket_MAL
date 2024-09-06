package com.g.pocketmal.ui.legacy.view

import com.g.pocketmal.ui.recommendations.RecommendedTitleViewEntity

interface RecommendationsView: com.g.pocketmal.ui.legacy.view.BaseSessionView {
    fun displayRecommendations(items: List<RecommendedTitleViewEntity>)
    fun displayNoRecommendations()
    fun displayError()
    fun showProgress()
    fun hideProgress()
}