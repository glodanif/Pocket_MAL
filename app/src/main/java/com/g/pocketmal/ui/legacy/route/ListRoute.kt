package com.g.pocketmal.ui.legacy.route

import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.ui.legacy.comparator.SortingType

interface ListRoute: com.g.pocketmal.ui.legacy.route.BaseSessionRoute {
    fun redirectToDetailsScreen(id: Int, type: TitleType)
    fun redirectToSearchScreen(type: TitleType)
    fun closeApp()

    fun displaySortingDialog(type: SortingType, reverse: Boolean)
}