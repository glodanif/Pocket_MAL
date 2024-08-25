package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.api.response.ErrorResponse
import com.g.pocketmal.data.keyvalue.SessionManager
import com.g.pocketmal.domain.entity.converter.LoginEntityConverter
import com.g.pocketmal.domain.exception.NotApprovedTitleException
import com.google.gson.Gson

class AuthorizationInteractor(
        private val apiService: ApiService,
        private val session: SessionManager,
        private val converter: LoginEntityConverter
) : BaseInteractor<AuthorizationInteractor.Params, Unit>() {

    override suspend fun execute(input: Params) {

        require(input.verifier.isNotEmpty()) { "Code verifier should not be empty" }

        val response = apiService.getAccessToken(input.code, input.verifier)

        val tokenResponse = response.body()
        if (response.isSuccessful && tokenResponse != null) {
            val token = converter.transform(tokenResponse)
            session.saveTokenData(token.accessToken, token.refreshToken,
                    System.currentTimeMillis() + token.expiresIn * 1000)

            val infoResponse = apiService.getMyInfo()
            val userResponse = infoResponse.body()
            if (infoResponse.isSuccessful && userResponse != null) {
                val info = converter.transform(userResponse)
                session.saveUser(info)
            } else {

                val errorResponse = infoResponse.errorBody()
                if (errorResponse != null) {
                    val body = errorResponse.string()
                    throw NetworkException(infoResponse.code(), errorMessage = body)
                }

                throw NetworkException(infoResponse.code(), errorMessage = "User profile request is not successful")
            }
        } else {

            val errorResponse = response.errorBody()
            if (errorResponse != null) {
                val body = errorResponse.string()
                throw NetworkException(response.code(), errorMessage = body)
            }

            throw NetworkException(response.code(), errorMessage = "Authorization request was not successful")
        }
    }

    class Params(
            val code: String,
            val verifier: String
    )
}