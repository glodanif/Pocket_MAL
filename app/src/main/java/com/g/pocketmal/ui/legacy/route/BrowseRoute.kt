package com.g.pocketmal.ui.legacy.route

import com.g.pocketmal.data.util.TitleType

interface BrowseRoute: com.g.pocketmal.ui.legacy.route.BaseSessionRoute {
    fun openDetailsScreen(id: Int, titleType: TitleType)
}