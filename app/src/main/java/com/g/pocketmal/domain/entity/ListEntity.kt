package com.g.pocketmal.domain.entity

import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.common.Status

data class ListEntity(
        val seriesId: Int,
        val seriesTitle: String,
        val seriesType: String,
        val seriesEpisodes: Int,
        val seriesSubEpisodes: Int,
        val seriesStatus: String,
        val seriesImage: String?,
        var myStartDate: String?,
        var myFinishDate: String?,
        var myEpisodes: Int,
        var mySubEpisodes: Int,
        var myScore: Int,
        var myStatus: Status,
        var myRe: Boolean,
        var myReValue: Int,
        var myReTimes: Int,
        var myLastUpdated: Long,
        var myTags: List<String>,
        val titleType: TitleType
)