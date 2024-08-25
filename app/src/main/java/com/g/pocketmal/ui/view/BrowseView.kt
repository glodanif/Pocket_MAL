package com.g.pocketmal.ui.view

import androidx.annotation.StringRes
import com.g.pocketmal.ui.viewmodel.BrowseItemViewModel
import com.g.pocketmal.ui.viewmodel.RankedItemViewModel

interface BrowseView: BaseSessionView {
    fun setToolbarTitle(@StringRes title: Int)
    fun addItemsToList(items: List<BrowseItemViewModel>)
    fun notifyLoadingFailure()
    fun displayFailPopup()
    fun showProgressFooter()
    fun hideProgressFooter()
}