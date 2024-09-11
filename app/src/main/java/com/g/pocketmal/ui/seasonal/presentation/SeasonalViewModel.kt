package com.g.pocketmal.ui.seasonal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.repository.SeasonalRepository
import com.g.pocketmal.data.repository.SeasonalResult
import com.g.pocketmal.data.util.PartOfYear
import com.g.pocketmal.util.AnimeSeason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SeasonalViewModel @Inject constructor(
    private val repository: SeasonalRepository,
    private val converter: SeasonalSectionConverter,
) : ViewModel() {

    private val calendar = Calendar.getInstance()
    private val _season = MutableStateFlow(
        Season(
            calendar.get(Calendar.YEAR),
            AnimeSeason.parseSeason(calendar.get(Calendar.MONTH)),
        )
    )
    val season = _season.asStateFlow()
    private val _seasonalState = MutableStateFlow<SeasonalState>(SeasonalState.Loading)
    val seasonalState = _seasonalState.asStateFlow()

    fun setNewSeason(season: Season) {
        _season.value = season
    }

    fun loadSeason(season: Season) {
        _seasonalState.value = SeasonalState.Loading
        viewModelScope.launch {
            val response = repository.getSeasonalAnime(season.year, season.partOfYear)
            when (response) {
                SeasonalResult.EmptyResult -> {
                    _seasonalState.value = SeasonalState.EmptySeason
                }

                is SeasonalResult.NetworkError -> {
                    _seasonalState.value = SeasonalState.FailedToLoad(response.message)
                }

                is SeasonalResult.Result -> {
                    val seasonalList = converter.transform(response.seasonalAnime, season)
                    _seasonalState.value = SeasonalState.SeasonalList(seasonalList)
                }
            }
        }
    }
}
