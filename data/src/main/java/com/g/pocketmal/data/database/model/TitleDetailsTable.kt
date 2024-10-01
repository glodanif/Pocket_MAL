package com.g.pocketmal.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.data.api.response.*
import com.g.pocketmal.data.api.response.RelatedTitleEdge
import com.g.pocketmal.data.common.Broadcast
import com.g.pocketmal.data.common.Company
import com.g.pocketmal.data.common.Genre
import com.g.pocketmal.data.common.StartSeason

@Entity(tableName = "title_details")
class TitleDetailsTable(
    @PrimaryKey(autoGenerate = true)
    var uid: Long,
    var id: Int,
    @ColumnInfo(name = "title_type")
    var titleType: DataTitleType,
    @ColumnInfo(name = "start_date")
    var startDate: String?,
    @ColumnInfo(name = "finish_date")
    var finishDate: String?,
    var type: String,
    var status: String,
    var episodes: Int,
    @ColumnInfo(name = "sub_episodes")
    var subEpisodes: Int,
    @ColumnInfo(name = "image_url")
    var imageUrl: String?,
    var synopsis: String?,
    var title: String,
    @ColumnInfo(name = "english_title")
    var englishTitle: String?,
    var synonyms: List<String>?,
    @ColumnInfo(name = "japanese_title")
    var japaneseTitle: String?,
    var score: Float?,
    @ColumnInfo(name = "scored_users")
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
    @ColumnInfo(name = "manga_authors")
    val mangaAuthors: List<PersonRoleEdge>?,
    @ColumnInfo(name = "anime_studios")
    val animeStudios: List<Company>?,
    val genres: List<Genre>,
    @ColumnInfo(name = "related_anime")
    val relatedAnime: List<RelatedTitleEdge>?,
    @ColumnInfo(name = "related_manga")
    val relatedManga: List<RelatedTitleEdge>?,
    @ColumnInfo(name = "opening_themes")
    val openingThemes: List<String>?,
    @ColumnInfo(name = "ending_themes")
    val endingThemes: List<String>?
)
