package com.g.pocketmal.ui.recommendations.presentation

import com.g.pocketmal.ui.common.inliststatus.InListStatusViewEntity

class RecommendedTitleViewEntity(
        val id: Int,
        val title: String,
        val poster: String?,
        val recommendationsCount: String,
        val details: String,
        val score: String,
        val inListStatus: InListStatusViewEntity,
)