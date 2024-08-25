package com.g.pocketmal.data.api.response

import com.g.pocketmal.data.common.Broadcast
import com.g.pocketmal.data.common.Company
import com.g.pocketmal.data.common.Genre
import com.g.pocketmal.data.common.StartSeason
import com.google.gson.annotations.SerializedName

data class TitleDetailsResponse(
        val id: Int,
        val title: String,
        @SerializedName("main_picture")
        val mainPicture: Picture?,
        @SerializedName("alternative_titles")
        val alternativeTitles: AlternativeTitles?,
        @SerializedName("start_date")
        val startDate: String?,
        @SerializedName("end_date")
        val endDate: String?,
        val synopsis: String?,
        @SerializedName("mean")
        val meanScore: Float?,
        val rank: Int?,
        val popularity: Int?,
        @SerializedName("num_list_users")
        val numListUsers: Int,
        @SerializedName("num_scoring_users")
        val numScoringUsers: Int,
        val nsfw: String?,
        val genres: List<Genre>,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("media_type")
        val mediaType: String,
        val status: String,
        @SerializedName("my_list_status")
        val myListStatus: ListStatus?,
        @SerializedName("num_episodes")
        val numEpisodes: Int,
        @SerializedName("num_volumes")
        val numVolumes: Int,
        @SerializedName("num_chapters")
        val numChapters: Int,
        @SerializedName("start_season")
        val startSeason: StartSeason?,
        val broadcast: Broadcast?,
        val source: String?,
        @SerializedName("average_episode_duration")
        val averageEpisodeDuration: Int?,
        val rating: String?,
        val studios: List<Company>,
        val authors: List<PersonRoleEdge>,
        val pictures: List<Picture>,
        val background: String?,
        @SerializedName("related_anime")
        val relatedAnime: List<RelatedTitleEdge>,
        @SerializedName("related_manga")
        val relatedManga: List<RelatedTitleEdge>,
        val serialization: List<MangaMagazineRelationEdge>
)
