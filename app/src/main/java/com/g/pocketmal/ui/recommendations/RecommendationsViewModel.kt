package com.g.pocketmal.ui.recommendations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.DataOutput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    private val apiService: ApiService,
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
            val response = apiService.getRecommendations(titleId, titleType)
            when (response) {
                is DataOutput.Success -> {
                    val result = converter.transform(response.output, titleType)
                    if (result.isNotEmpty()) {
                        _recommendationsState.value = RecommendationsState.RecommendationsList(result)
                    } else {
                        _recommendationsState.value = RecommendationsState.NoRecommendations
                    }
                }
                is DataOutput.Error -> {
                    _recommendationsState.value = RecommendationsState
                        .FailedToLoad("Recommendations request was not successful")
                }
            }
        }
    }
}
