package com.g.pocketmal.ui.search.presentation

sealed class SearchState {
    data class IncorrectInput(val minQueryLength: Int) : SearchState()
    data object NoSearchResult : SearchState()
    data class SearchResult(val result: List<SearchResultViewEntity>) : SearchState()
    data object Loading : SearchState()
    data object Initial : SearchState()
    data class FailedToLoad(val errorMessage: String) : SearchState()
}
