package com.g.pocketmal.ui.route

import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.comparator.SortingType

interface ListRoute: BaseSessionRoute {
    fun redirectToDetailsScreen(id: Int, type: TitleType)
    fun redirectToSearchScreen(type: TitleType)
    fun closeApp()

    fun displaySortingDialog(type: SortingType, reverse: Boolean)
}