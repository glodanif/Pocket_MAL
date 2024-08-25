package com.g.pocketmal.ui.view

import com.g.pocketmal.data.util.PartOfYear
import com.g.pocketmal.ui.viewmodel.SeasonalSectionViewModel

interface SeasonalView: BaseSessionView {
    fun displaySeasonalAnime(items: List<SeasonalSectionViewModel>)
    fun displaySeason(year: Int, partOfYear: PartOfYear)
    fun showEmptySeason()
    fun askToWait()
    fun displayFailPopup()
    fun hideFailPopup()
    fun showProgress()
    fun hideProgress()
}