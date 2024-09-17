package com.g.pocketmal.ui.ranked.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.repository.BrowseRepository
import com.g.pocketmal.data.repository.BrowseResult
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.domain.TitleType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankedViewModel @Inject constructor(
    private val rankedRepository: BrowseRepository,
    private val converter: RankedItemConverter,
) : ViewModel() {

    private val _rankedState = MutableStateFlow<RankedState>(RankedState.Loading)
    val rankedState = _rankedState.asStateFlow()

    fun loadRankedTitles(rankedType: RankingType, titleType: TitleType, offset: Int = 0) {
        _rankedState.value = RankedState.Loading
        viewModelScope.launch {
            val result = rankedRepository.loadRankedTitles(titleType, rankedType, offset)
            when (result) {
                BrowseResult.EmptyResult -> {

                }
                is BrowseResult.NetworkError -> {
                    _rankedState.value = RankedState.Error(result.message)
                }
                is BrowseResult.Result -> {
                    val items = converter.transform(result.rankedItems, titleType)
                    _rankedState.value = RankedState.RankedItems(items)
                }
            }
        }
    }
}
