package com.g.pocketmal.data.api

import com.g.pocketmal.data.api.response.*
import retrofit2.Response

import java.util.HashMap

import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface MalApi {

    @GET("users/@me")
    suspend fun getMyInfo(): Response<BasicUserResponse>

    @GET("users/@me?fields=id,name,picture,gender,birthday,location,joined_at,anime_statistics,manga_statistics,time_zone,is_supporter")
    suspend fun getMyDetailedInfo(): Response<UserResponse>

    @FormUrlEncoded
    @POST
    suspend fun getAccessToken(
            @Url url: String,
            @Field("client_id") clientId: String,
            @Field("grant_type") grantType: String,
            @Field("code") code: String,
            @Field("redirect_uri") redirectUri: String,
            @Field("code_verifier") codeVerifier: String
    ): Response<TokenResponse>

    @GET
    suspend fun getListPage(
            @Url url: String
    ): Response<ListResponse>

    @GET("{type}/{id}?fields=alternative_titles,media_type,num_episodes,num_chapters,num_volumes,status,start_date,end_date,start_season,broadcast," +
            "source,genres,average_episode_duration,rating,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,authors{first_name,last_name}," +
            "created_at,updated_at,studios,background,num_episodes_in_db,pictures,serialization,related_anime,related_manga," +
            "my_list_status{start_date,finish_date,num_times_rewatched,num_times_reread,tags}")
    suspend fun getTitleDetails(
            @Path("type") type: String,
            @Path("id") titleId: Int
    ): Response<TitleDetailsResponse>

    @GET
    suspend fun getRecommendations(
            @Url url: String
    ): Response<RecommendationsResponse>

    @GET("users/{userId}")
    suspend fun getUserInfo(
            @Path("userId") userId: String
    ): Response<UserResponse>

    @DELETE("anime/{id}/my_list_status")
    suspend fun deleteAnime(
            @Path("id") titleId: Int
    ): Response<Unit>

    @DELETE("manga/{id}/my_list_status")
    suspend fun deleteManga(
            @Path("id") titleId: Int
    ): Response<Unit>

    @FormUrlEncoded
    @PUT("anime/{id}/my_list_status")
    suspend fun addAnime(
            @Path("id") titleId: Int,
            @Field("status") status: String,
            @Field("score") score: Int,
            @Field("num_watched_episodes") episodes: Int,
            @Field("is_rewatching") isRe: Boolean
    ): Response<ListStatus>

    @FormUrlEncoded
    @PUT("manga/{id}/my_list_status")
    suspend fun addManga(
            @Path("id") titleId: Int,
            @Field("status") status: String,
            @Field("score") score: Int,
            @Field("num_chapters_read") chapters: Int,
            @Field("num_volumes_read") volumes: Int,
            @Field("is_rereading") isRe: Boolean
    ): Response<ListStatus>

    @FormUrlEncoded
    @PATCH("anime/{id}/my_list_status")
    suspend fun updateAnime(
            @Path("id") titleId: Int,
            @FieldMap fields: HashMap<String, Any>
    ): Response<ListStatus>

    @FormUrlEncoded
    @PATCH("manga/{id}/my_list_status")
    suspend fun updateManga(
            @Path("id") titleId: Int,
            @FieldMap fields: HashMap<String, Any>
    ): Response<ListStatus>

    @GET("anime/season/{year}/{season}?limit=500&sort=anime_num_list_users&" +
            "fields=mean,media_type,num_episodes,broadcast,source,genres," +
            "synopsis,num_list_users,nsfw,start_season,start_date,studios,alternative_titles")
    suspend fun getSeasonalAnime(
            @Path("year") year: Int,
            @Path("season") season: String,
            @Query("nsfw") nsfw: Boolean
    ): Response<SeasonResponse>

    @GET("{type}?fields=alternative_titles,mean,nsfw,media_type,num_episodes,num_chapters,num_volumes,synopsis&limit=100")
    suspend fun search(
            @Path("type") type: String,
            @Query("nsfw") nsfw: Boolean,
            @Query("q") query: String
    ): Response<SearchResponse>

    @GET("{type}/ranking?fields=score,media_type,num_list_users,num_episodes,num_chapters,mean,start_date,synopsis,alternative_titles")
    suspend fun getRankingList(
            @Path("type") type: String,
            @Query("ranking_type") rankingType: String,
            @Query("limit") limit: Int,
            @Query("offset") offset: Int,
            @Query("nsfw") nsfw: Boolean
    ): Response<RankingResponse>

    companion object {
        const val MAL_HOST = "https://myanimelist.net/"
        const val AUTH_BASE_URL = MAL_HOST + "v1/oauth2/"
        const val API_BASE_URL = "https://api.myanimelist.net/v2/"
        const val LIST_PAGE_LIMIT = 1000
        const val BROWSE_PAGE_LIMIT = 50
    }
}
