package com.g.pocketmal.domain.entity

import com.g.pocketmal.domain.TitleType

data class TitleDetails(
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
    val serialization: List<Magazine>?,
    val source: String?,
    val broadcast: Broadcast?,
    val premiered: StartSeason?,
    val mangaAuthors: List<Person>?,
    val animeStudios: List<Company>?,
    val genres: List<Genre>,
    val relatedAnime: List<RelatedTitle>?,
    val relatedManga: List<RelatedTitle>?,
)