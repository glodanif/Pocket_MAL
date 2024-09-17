package com.g.pocketmal.ui.ranked.presentation

sealed class RankedState {
    data object Loading : RankedState()
    data object LoadingMore : RankedState()
    data class Error(val message: String) : RankedState()
    data class RankedItems(val items: List<RankedItemViewEntity>) : RankedState()
}
