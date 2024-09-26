package com.g.pocketmal.ui.list.presentation

import androidx.annotation.StringRes
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.util.EpisodeType

data class RecordListViewEntity(
        val seriesId: Int,
        val seriesTitle: String,
        val seriesMediaType: String,
        val seriesMediaTypeRaw: String,
        val seriesPosterUrl: String?,
        val seriesEpisodes: Int,
        val seriesSubEpisodes: Int,
        val seriesStatus: String,
        val isSubEpisodesAvailable: Boolean,
        val myEpisodes: Int,
        val mySubEpisodes: Int,
        val myTags: String?,
        val episodesLabel: String,
        val subEpisodesLabel: String,
        val episodesName: String,
        val subEpisodesName: String,
        @StringRes
        val allEpisodesFinishedText: Int,
        val myScore: Int,
        val myScoreLabel: String,
        val isRe: Boolean,
        val reLabel: String,
        val lastUpdated: Long,
        val episodesType: EpisodeType,
        val subEpisodesType: EpisodeType,
        val seriesDetails: String,
        val titleType: TitleType,
)
