package com.g.pocketmal.ui.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.repository.SearchRepository
import com.g.pocketmal.domain.result.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val converter: SearchResultConverter,
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchState = _searchState.asStateFlow()

    fun search(query: String, titleType: TitleType) {
        _searchState.value = SearchState.Loading
        viewModelScope.launch {
            val response = repository.search(query, titleType)
            when (response) {
                SearchResult.EmptyResult -> {
                    _searchState.value = SearchState.NoSearchResult
                }

                is SearchResult.InvalidQuery -> {
                    _searchState.value = SearchState.IncorrectInput(response.minCharacters)
                }

                is SearchResult.NetworkError -> {
                    _searchState.value = SearchState.FailedToLoad(errorMessage = response.message)
                }

                is SearchResult.Result -> {
                    val result = converter.transform(response.searchResult, titleType)
                    _searchState.value = SearchState.SearchResult(result = result)
                }
            }
        }
    }
}
