package com.g.pocketmal.ui.view

import com.g.pocketmal.ui.viewmodel.SearchResultViewModel

interface SearchView: BaseSessionView {
    fun showProgress()
    fun hideProgress()
    fun showFailPopup()
    fun hideFailPopup()
    fun displaySearchResult(list: List<SearchResultViewModel>)
    fun displayEmptyResult()
    fun showQueryIsTooShortMessage()
}