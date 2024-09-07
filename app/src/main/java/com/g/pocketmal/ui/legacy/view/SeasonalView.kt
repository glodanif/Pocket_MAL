package com.g.pocketmal.ui.legacy.view

import com.g.pocketmal.data.util.PartOfYear

interface SeasonalView: com.g.pocketmal.ui.legacy.view.BaseSessionView {
    fun displaySeasonalAnime(items: List<com.g.pocketmal.ui.legacy.viewentity.SeasonalSectionViewModel>)
    fun displaySeason(year: Int, partOfYear: PartOfYear)
    fun showEmptySeason()
    fun askToWait()
    fun displayFailPopup()
    fun hideFailPopup()
    fun showProgress()
    fun hideProgress()
}