package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.SessionStorage
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.domain.entity.AuthUrl
import com.g.pocketmal.domain.entity.BaseUser
import com.g.pocketmal.domain.repository.RecordRepository
import com.g.pocketmal.domain.repository.SessionRepository

class SessionRepositoryImplementation(
    private val sessionStorage: SessionStorage,
    private val recordRepository: RecordRepository,
    private val sharingPatterns: SharingPatternDispatcher,
    private val localStorage: LocalStorage,
    private val apiService: ApiService,
) : SessionRepository {

    override fun isUserLoggedIn(): Boolean {
        return sessionStorage.accessToken != null && sessionStorage.user != null
    }

    override fun getCurrentUser(): BaseUser? = sessionStorage.user

    override fun generateAuthData(): AuthUrl {
        val authUrl = apiService.getAuthUrl()
        return AuthUrl(
            authUrl.url,
            authUrl.codeVerifier,
            authUrl.redirectUrl
        )
    }

    override suspend fun authorize(code: String, verifier: String) {
        require(verifier.isNotEmpty()) { "Code verifier should not be empty" }

        val response = apiService.getAccessToken(code, verifier)

        val tokenResponse = response.body()
        if (response.isSuccessful && tokenResponse != null) {
            val token = com.g.pocketmal.domain.entity.AuthToken(
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
                val info = com.g.pocketmal.domain.entity.BaseUser(
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

    override suspend fun logout() {
        recordRepository.dropAllRecords()
        localStorage.reset()
        sessionStorage.logout()
        sharingPatterns.reset()
    }
}
