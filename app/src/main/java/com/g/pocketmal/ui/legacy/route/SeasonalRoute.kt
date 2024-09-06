package com.g.pocketmal.ui.legacy.route

import com.g.pocketmal.data.util.Season

interface SeasonalRoute: com.g.pocketmal.ui.legacy.route.BaseSessionRoute {
    fun openDetailsScreen(id: Int)
    fun openSeasonPicker(season: Season)
}