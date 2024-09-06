package com.g.pocketmal.ui.legacy.view

import androidx.annotation.StringRes

interface BrowseView: com.g.pocketmal.ui.legacy.view.BaseSessionView {
    fun setToolbarTitle(@StringRes title: Int)
    fun addItemsToList(items: List<com.g.pocketmal.ui.legacy.viewmodel.BrowseItemViewModel>)
    fun notifyLoadingFailure()
    fun displayFailPopup()
    fun showProgressFooter()
    fun hideProgressFooter()
}