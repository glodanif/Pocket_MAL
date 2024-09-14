package com.g.pocketmal.ui.legacy.route

import com.g.pocketmal.domain.TitleType

interface RankedRoute: com.g.pocketmal.ui.legacy.route.BaseSessionRoute {
    fun openDetailsScreen(id: Int, titleType: TitleType)
}