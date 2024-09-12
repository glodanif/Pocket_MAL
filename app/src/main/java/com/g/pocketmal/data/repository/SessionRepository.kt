package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.SessionStorage
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.data.platform.CookieManager
import com.g.pocketmal.data.platform.NetworkManager
import com.g.pocketmal.domain.entity.AuthUrlEntity
import com.g.pocketmal.domain.entity.BaseUserEntity
import com.g.pocketmal.domain.entity.TokenEntity
import com.g.pocketmal.util.list.ListsManager
import java.io.IOException

class SessionRepository(
    private val sessionStorage: SessionStorage,
    private val recordRepository: RecordRepository,
    private val listsManager: ListsManager,
    private val sharingPatterns: SharingPatternDispatcher,
    private val localStorage: LocalStorage,
    private val apiService: ApiService,
    private val networkManager: NetworkManager,
    private val cookieManager: CookieManager,
) {

    fun isUserLoggedIn(): Boolean {
        return sessionStorage.accessToken != null && sessionStorage.user != null
    }

    fun generateAuthData(): AuthUrlEntity {
        cookieManager.clearCookies()
        val authUrl = apiService.getAuthUrl()
        if (networkManager.isNetworkAvailable()) {
            return AuthUrlEntity(authUrl.url, authUrl.codeVerifier, authUrl.redirectUrl)
        } else {
            throw IOException("No internet connection")
        }
    }

    suspend fun authorize(code: String, verifier: String) {
        require(verifier.isNotEmpty()) { "Code verifier should not be empty" }

        val response = apiService.getAccessToken(code, verifier)

        val tokenResponse = response.body()
        if (response.isSuccessful && tokenResponse != null) {
            val token = TokenEntity(
                tokenResponse.tokenType,
                tokenResponse.expiresIn,
                tokenResponse.accessToken,
                tokenResponse.refreshToken,
            )
            sessionStorage.saveTokenData(
                token.accessToken, token.refreshToken,
                System.currentTimeMillis() + token.expiresIn * 1000
            )

            val infoResponse = apiService.getMyInfo()
            val userResponse = infoResponse.body()
            if (infoResponse.isSuccessful && userResponse != null) {
                val info = BaseUserEntity(
                    userResponse.id,
                    userResponse.name,
                    userResponse.picture ?: "",
                )
                sessionStorage.saveUser(info)
            } else {
                val errorResponse = infoResponse.errorBody()
                if (errorResponse != null) {
                    val body = errorResponse.string()
                    throw NetworkException(infoResponse.code(), errorMessage = body)
                }
                throw NetworkException(
                    infoResponse.code(),
                    errorMessage = "User profile request is not successful"
                )
            }
        } else {
            val errorResponse = response.errorBody()
            if (errorResponse != null) {
                val body = errorResponse.string()
                throw NetworkException(response.code(), errorMessage = body)
            }
            throw NetworkException(
                response.code(),
                errorMessage = "Authorization request was not successful"
            )
        }
    }

    suspend fun logout() {
        recordRepository.dropAllRecords()
        localStorage.reset()
        sessionStorage.logout()
        listsManager.clearInstance()
        sharingPatterns.reset()
    }
}
