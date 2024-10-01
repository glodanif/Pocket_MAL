package com.g.pocketmal.ui.legacy.viewentity

import android.text.Spannable
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.entity.Genre
import com.g.pocketmal.domain.entity.RelatedTitle

class TitleDetailsViewModel(
    val id: Int,
    val titleType: TitleType,
    val startDate: String,
    val finishDate: String,
    val type: String,
    val status: String,
    val episodes: Int,
    val subEpisodes: Int,
    val imageUrl: String?,
    val synopsis: String,
    val title: String,
    val englishTitle: String?,
    val synonyms: String?,
    val japaneseTitle: String?,
    val score: Spannable,
    val scoredUsersCount: String,
    val ranked: String?,
    val genres: List<Genre>,
    val relatedTitles: List<RelatedTitle>,
    val screenTitle: String,
    val detailsList: List<Pair<String, String>>,
    val airingStats: String?,
    val episodesLabel: String,
    val subEpisodesLabel: String,
    val withSubEpisodes: Boolean,
)
