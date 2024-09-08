package com.g.pocketmal.ui.legacy.viewentity

import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.common.Status
import java.io.Serializable

class RecordViewModel(
        val seriesId: Int,
        val seriesTitle: String,
        val seriesMediaType: String,
        val seriesEpisodes: Int = 0,
        val seriesSubEpisodes: Int = 0,
        val seriesStatus: String,
        val seriesImage: String?,
        val myEpisodes: Int = 0,
        val mySubEpisodes: Int = 0,
        val myScore: Int = 0,
        val myStatus: Status = Status.NOT_IN_LIST,
        val myStatusLabel: String,
        val myRe: Boolean = false,
        val myReTimes: Int = 0,
        val myTags: String,
        val myStartDate: String,
        val myFinishDate: String,
        val myComments: String,
        val updatedAt: Long = 0,
        val recordType: TitleType,
        val seriesStatusText: String,
        val seriesTypeText: String,
        val discussionLink: String,
        val malLink: String,
        val myScoreLabel: String,
        val seriesEpisodesLabel: String,
        val seriesSubEpisodesLabel: String,
        val shortIncrementEpisodesLabel: String,
        val shortIncrementSubEpisodesLabel: String,
        val reLabel: String,
        val fullEpisodesLabel: String,
        val episodesTypeLabel: String,
        val subEpisodesTypeLabel: String,
        val withSubEpisodes: Boolean
) : Serializable
