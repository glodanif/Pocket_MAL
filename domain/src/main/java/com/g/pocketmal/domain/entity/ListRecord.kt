package com.g.pocketmal.domain.entity

import com.g.pocketmal.domain.InListStatus
import com.g.pocketmal.domain.TitleType

data class ListRecord(
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
    var myStatus: InListStatus,
    var myRe: Boolean,
    var myReValue: Int,
    var myReTimes: Int,
    var myLastUpdated: Long,
    var myTags: List<String>,
    var myComments: String?,
    var myPriority: Int,
    val titleType: TitleType
)