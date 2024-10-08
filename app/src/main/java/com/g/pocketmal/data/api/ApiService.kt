package com.g.pocketmal.data.api

import com.g.pocketmal.data.api.request.AuthUrl
import com.g.pocketmal.data.api.response.*
import com.g.pocketmal.data.util.PartOfYear
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.util.RankingType
import retrofit2.Response

import java.util.HashMap

interface ApiService {
    fun getAuthUrl(): AuthUrl
    suspend fun getMyInfo(): Response<BasicUserResponse>
    suspend fun getMyUserInfo(): Response<UserResponse>
    suspend fun getAccessToken(code: String, codeVerifier: String): Response<TokenResponse>
    suspend fun getFirstListPage(type: TitleType, includeNsfw: Boolean): Response<ListResponse>
    suspend fun getListPage(url: String): Response<ListResponse>
    suspend fun getUserInfo(userId: Int): Response<UserResponse>
    suspend fun getTitleDetails(id: Int, type: TitleType): Response<TitleDetailsResponse>
    suspend fun deleteTitle(titleId: Int, type: TitleType): Response<Unit>
    suspend fun addTitle(titleId: Int, type: TitleType): Response<ListStatus>
    suspend fun updateTitle(id: Int, params: HashMap<String, Any>, type: TitleType): Response<ListStatus>
    suspend fun getRankingList(titleType: TitleType, rankingType: RankingType, includeNsfw: Boolean, limit: Int, offset: Int): Response<RankingResponse>
    suspend fun search(query: String, type: TitleType, includeNsfw: Boolean): Response<SearchResponse>
    suspend fun getSeasonalAnime(partOfYear: PartOfYear, year: Int, includeNsfw: Boolean): Response<SeasonResponse>
    suspend fun getRecommendations(id: Int, type: TitleType): Response<RecommendationsResponse>
}
