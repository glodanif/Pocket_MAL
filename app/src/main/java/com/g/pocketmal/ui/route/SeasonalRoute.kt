package com.g.pocketmal.ui.route

import com.g.pocketmal.data.util.Season

interface SeasonalRoute: BaseSessionRoute {
    fun openDetailsScreen(id: Int)
    fun openSeasonPicker(season: Season)
}