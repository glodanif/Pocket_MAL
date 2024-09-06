package com.g.pocketmal.ui.legacy.route

import com.g.pocketmal.data.util.TitleType

interface RecommendationsRoute: com.g.pocketmal.ui.legacy.route.BaseSessionRoute {
    fun openDetailsScreen(id: Int, titleType: TitleType)
}