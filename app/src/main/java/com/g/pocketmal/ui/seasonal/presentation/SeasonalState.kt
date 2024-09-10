package com.g.pocketmal.ui.seasonal.presentation

sealed class SeasonalState {
    data object EmptySeason : SeasonalState()
    data class SeasonalList(val sections: List<SeasonalSectionViewEntity>) : SeasonalState()
    data object Loading : SeasonalState()
    data class FailedToLoad(val errorMessage: String) : SeasonalState()
}
