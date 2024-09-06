package com.g.pocketmal.ui.legacy.view

interface SearchView: com.g.pocketmal.ui.legacy.view.BaseSessionView {
    fun showProgress()
    fun hideProgress()
    fun showFailPopup()
    fun hideFailPopup()
    fun displaySearchResult(list: List<com.g.pocketmal.ui.legacy.viewmodel.SearchResultViewModel>)
    fun displayEmptyResult()
    fun showQueryIsTooShortMessage()
}