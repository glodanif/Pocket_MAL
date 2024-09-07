package com.g.pocketmal.ui.recommendations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.repository.RecommendationsRepository
import com.g.pocketmal.data.repository.RecommendationsResult
import com.g.pocketmal.data.util.TitleType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    private val repository: RecommendationsRepository,
    private val converter: RecommendedTitleConverter,
) : ViewModel() {

    private val _recommendationsState =
        MutableStateFlow<RecommendationsState>(RecommendationsState.Loading)
    val recommendationsState = _recommendationsState.asStateFlow()

    fun loadRecommendations(titleId: Int?, titleType: TitleType) {
        _recommendationsState.value = RecommendationsState.Loading
        if (titleId == null) {
            _recommendationsState.value = RecommendationsState.IncorrectInput
            return
        }

        viewModelScope.launch {
            val response = repository.getRecommendations(titleId, titleType)
            when (response) {
                is RecommendationsResult.Result -> {
                    val result = converter.transform(response.recommendations, titleType)
                    _recommendationsState.value = RecommendationsState.RecommendationsList(result)
                }

                RecommendationsResult.EmptyResult -> {
                    _recommendationsState.value = RecommendationsState.NoRecommendations
                }

                is RecommendationsResult.NetworkError -> {
                    _recommendationsState.value =
                        RecommendationsState.FailedToLoad("Recommendations request was not successful")
                }
            }
        }
    }
}
