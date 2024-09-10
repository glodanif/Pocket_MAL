package com.g.pocketmal.data.api

import com.g.pocketmal.BuildConfig
import com.g.pocketmal.data.api.request.AuthUrl
import com.g.pocketmal.data.api.request.OAuthConfig
import com.g.pocketmal.data.api.response.BasicUserResponse
import com.g.pocketmal.data.api.response.ListResponse
import com.g.pocketmal.data.api.response.ListStatus
import com.g.pocketmal.data.api.response.RankingResponse
import com.g.pocketmal.data.api.response.RecommendationsResponse
import com.g.pocketmal.data.api.response.SearchResponse
import com.g.pocketmal.data.api.response.SeasonResponse
import com.g.pocketmal.data.api.response.TitleDetailsResponse
import com.g.pocketmal.data.api.response.TokenResponse
import com.g.pocketmal.data.api.response.UserResponse
import com.g.pocketmal.data.keyvalue.SessionManager
import com.g.pocketmal.data.util.PKCEHelper
import com.g.pocketmal.data.util.PartOfYear
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.util.TitleType.ANIME
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class MalApiService(sessionManager: SessionManager, private val oAuthConfig: OAuthConfig) :
    ApiService {

    private val apiService: MalApi
    private val pkceHelper = PKCEHelper()

    init {

        val builder = OkHttpClient.Builder().apply {
            addInterceptor(TokenInterceptor(sessionManager, oAuthConfig))
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(interceptor)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(MalApi.API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()

        apiService = retrofit.create(MalApi::class.java)
    }

    override fun getAuthUrl(): AuthUrl {

        val codeVerifier = pkceHelper.generateCodeVerifier()
        val codeChallenge = pkceHelper.generateCodeChallenge(codeVerifier)

        val url = MalApi.AUTH_BASE_URL + "authorize?" +
                "response_type=code&" +
                "client_id=" + oAuthConfig.clientId + "&" +
                "redirect_uri=" + oAuthConfig.redirectUrl + "&" +
                "code_challenge_method=plain&" +
                "code_challenge=" + codeChallenge + "&" +
                "state=pocket_mal_app"

        return AuthUrl(url, codeChallenge, oAuthConfig.redirectUrl)
    }

    override suspend fun getMyInfo(): Response<BasicUserResponse> = apiService.getMyInfo()

    override suspend fun getMyUserInfo(): Response<UserResponse> = apiService.getMyDetailedInfo()

    override suspend fun getAccessToken(
        code: String,
        codeVerifier: String
    ): Response<TokenResponse> {
        val url = MalApi.AUTH_BASE_URL + "token"
        return apiService.getAccessToken(
            url,
            oAuthConfig.clientId,
            "authorization_code",
            code,
            oAuthConfig.redirectUrl,
            codeVerifier
        )
    }

    override suspend fun getFirstListPage(
        type: TitleType,
        includeNsfw: Boolean
    ): Response<ListResponse> {
        val typePath = if (type == ANIME) "animelist" else "mangalist"
        val nsfw = if (includeNsfw) "1" else "0"
        val url =
            "${MalApi.API_BASE_URL}users/@me/$typePath?fields=my_list_status{start_date,finish_date,num_times_rewatched,num_times_reread,rewatch_value,reread_value,tags,comments,priority}," +
                    "media_type,num_episodes,num_chapters,num_volumes,status,broadcast,alternative_titles&nsfw=$nsfw&limit=${MalApi.LIST_PAGE_LIMIT}"
        return getListPage(url)
    }

    override suspend fun getListPage(url: String): Response<ListResponse> {
        return apiService.getListPage(url)
    }

    override suspend fun getTitleDetails(id: Int, type: TitleType): Response<TitleDetailsResponse> {
        return apiService.getTitleDetails(if (type == ANIME) "anime" else "manga", id)
    }

    override suspend fun getUserInfo(userId: Int): Response<UserResponse> =
        apiService.getUserInfo(userId.toString())

    override suspend fun deleteTitle(titleId: Int, type: TitleType): Response<Unit> =
        if (type == ANIME) {
            apiService.deleteAnime(titleId)
        } else {
            apiService.deleteManga(titleId)
        }

    override suspend fun addTitle(titleId: Int, type: TitleType): Response<ListStatus> =
        if (type == ANIME) {
            apiService.addAnime(titleId, "plan_to_watch", 0, 0, false)
        } else {
            apiService.addManga(titleId, "plan_to_read", 0, 0, 0, false)
        }

    override suspend fun updateTitle(
        id: Int,
        params: HashMap<String, Any>,
        type: TitleType
    ): Response<ListStatus> {
        return if (type == ANIME) {
            apiService.updateAnime(id, params)
        } else {
            apiService.updateManga(id, params)
        }
    }

    override suspend fun getRankingList(
        titleType: TitleType,
        rankingType: RankingType,
        includeNsfw: Boolean,
        limit: Int,
        offset: Int
    ): Response<RankingResponse> {

        val titleTypeValue = if (titleType == ANIME) "anime" else "manga"
        val topTypeValue = when (rankingType) {
            RankingType.UPCOMING -> "upcoming"
            RankingType.AIRING -> "airing"
            RankingType.BY_POPULARITY -> "bypopularity"
            else -> "all"
        }

        return apiService.getRankingList(titleTypeValue, topTypeValue, limit, offset, includeNsfw)
    }

    override suspend fun search(
        query: String,
        type: TitleType,
        includeNsfw: Boolean
    ): Response<SearchResponse> {
        val typePath = if (type == ANIME) "anime" else "manga"
        return apiService.search(typePath, includeNsfw, query)
    }

    override suspend fun getSeasonalAnime(
        partOfYear: PartOfYear,
        year: Int,
        includeNsfw: Boolean
    ): Response<SeasonResponse> {
        return apiService.getSeasonalAnime(year, partOfYear.season, includeNsfw)
    }

    override suspend fun getRecommendations(
        id: Int,
        type: TitleType
    ): Response<RecommendationsResponse> {
        val typePath = if (type == ANIME) "anime" else "manga"
        return apiService.getRecommendations(typePath, id)
    }
}
