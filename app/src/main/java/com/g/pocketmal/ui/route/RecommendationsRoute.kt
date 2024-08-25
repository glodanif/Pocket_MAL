package com.g.pocketmal.ui.route

import com.g.pocketmal.data.util.TitleType

interface RecommendationsRoute: BaseSessionRoute {
    fun openDetailsScreen(id: Int, titleType: TitleType)
}