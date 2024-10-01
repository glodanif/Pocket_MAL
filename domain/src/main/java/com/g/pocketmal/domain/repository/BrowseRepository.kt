package com.g.pocketmal.domain.repository

import com.g.pocketmal.domain.ExploreType
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.result.BrowseResult

interface BrowseRepository {
    suspend fun loadRankedTitles(
        titleType: TitleType,
        exploreType: ExploreType,
        offset: Int,
    ): BrowseResult
}
