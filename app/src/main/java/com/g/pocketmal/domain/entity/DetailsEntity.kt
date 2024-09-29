package com.g.pocketmal.domain.entity

import com.g.pocketmal.data.api.response.MangaMagazineRelationEdge
import com.g.pocketmal.data.api.response.PersonRoleEdge
import com.g.pocketmal.data.api.response.RelatedTitleEdge
import com.g.pocketmal.data.common.Broadcast
import com.g.pocketmal.data.common.Company
import com.g.pocketmal.data.common.Genre
import com.g.pocketmal.data.common.StartSeason
import com.g.pocketmal.domain.TitleType

data class DetailsEntity(
    var id: Int,
    var titleType: TitleType,
    var startDate: String?,
    var finishDate: String?,
    var type: String,
    var status: String,
    var episodes: Int,
    var subEpisodes: Int,
    var imageUrl: String?,
    var synopsis: String?,
    var title: String,
    var englishTitle: String?,
    var synonyms: List<String>?,
    var japaneseTitle: String?,
    var score: Float?,
    val scoredUsersCount: Int,
    val ranked: Int?,
    val popularity: Int?,
    val members: Int,
    val favorites: String,
    val rating: String?,
    val duration: Int?,
    val serialization: List<MangaMagazineRelationEdge>?,
    val source: String?,
    val broadcast: Broadcast?,
    val premiered: StartSeason?,
    val mangaAuthors: List<PersonRoleEdge>?,
    val animeStudios: List<Company>?,
    val genres: List<Genre>,
    val relatedAnime: List<RelatedTitleEdge>?,
    val relatedManga: List<RelatedTitleEdge>?,
)
