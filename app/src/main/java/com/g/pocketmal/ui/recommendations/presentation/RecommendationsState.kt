package com.g.pocketmal.ui.recommendations.presentation

sealed class RecommendationsState {

    data object IncorrectInput: RecommendationsState()

    data object NoRecommendations: RecommendationsState()

    data class RecommendationsList(
        val recommendations: List<RecommendedTitleViewEntity>
    ): RecommendationsState()

    data object Loading: RecommendationsState()

    data class FailedToLoad(
        val errorMessage: String,
    ): RecommendationsState()
}
