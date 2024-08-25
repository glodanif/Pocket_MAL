package com.g.pocketmal.ui.view

import androidx.annotation.StringRes
import com.g.pocketmal.ui.viewmodel.RankedItemViewModel

interface RankedView: BaseSessionView {
    fun setToolbarTitle(@StringRes title: Int)
    fun addItemsToList(items: List<RankedItemViewModel>)
    fun notifyLoadingFailure()
    fun displayFailPopup()
    fun showProgressFooter()
    fun hideProgressFooter()
}