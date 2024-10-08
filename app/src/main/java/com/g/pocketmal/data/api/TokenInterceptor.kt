package com.g.pocketmal.data.api

import com.g.pocketmal.data.api.request.OAuthConfig
import com.g.pocketmal.data.api.response.ErrorResponse
import com.g.pocketmal.data.api.response.TokenResponse
import com.g.pocketmal.data.keyvalue.SessionManager
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class TokenInterceptor internal constructor(
        private val sessionManager: SessionManager,
        private val oAuthConfig: OAuthConfig
) : Interceptor {

    private val gson = Gson()
    private val httpClient = OkHttpClient()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val builder = request.newBuilder()

        var logoutAfter = false

        synchronized(httpClient) {

            if (sessionManager.expirationTime > 0 && sessionManager.expirationTime < System.currentTimeMillis()) {
                val code = refreshToken() / 100
                if (code == 4) {
                    logoutAfter = true
                }
            }
        }

        val accessToken = sessionManager.accessToken
        if (accessToken != null) {
            builder.header("Authorization", "Bearer $accessToken")
        }

        request = builder.build()
        val response = chain.proceed(request)
        if (logoutAfter) {
            sessionManager.logout()
            throw SessionExpiredException()
        }

        if (response.code == 401) {
            val body = response.body
            if (body != null) {
                val error = gson.fromJson(body.string(), ErrorResponse::class.java)
                if (ErrorResponse.INVALID_TOKEN == error.error) {

                    synchronized(httpClient) {

                        val currentToken = sessionManager.accessToken

                        if (currentToken != null && currentToken == accessToken) {

                            val code = refreshToken() / 100
                            if (code != 2) {
                                if (code == 4) {
                                    sessionManager.logout()
                                    throw SessionExpiredException()
                                }
                                return response
                            }
                        }

                        val freshToken = sessionManager.accessToken
                        if (freshToken != null) {
                            builder.header("Authorization", String.format("Bearer %s", freshToken))
                            request = builder.build()
                            return chain.proceed(request)
                        }
                    }
                }
            }
        }

        return response
    }

    @Throws(IOException::class)
    private fun refreshToken(): Int {

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("client_id", oAuthConfig.clientId)
                .addFormDataPart("grant_type", "refresh_token")
                .addFormDataPart("refresh_token", sessionManager.refreshToken ?: "")
                .build()

        val request = Request.Builder()
                .url(MalApi.AUTH_BASE_URL + "token")
                .post(requestBody)
                .build()

        val response = httpClient.newCall(request).execute()
        val responseBody = response.body
        if (response.isSuccessful && responseBody != null) {
            try {
                val tokenResponse = gson.fromJson(responseBody.string(), TokenResponse::class.java)
                sessionManager.saveTokenData(tokenResponse.accessToken, tokenResponse.refreshToken,
                        System.currentTimeMillis() + tokenResponse.expiresIn * 1000)
            } catch (e: Exception) {
                return 400
            }
        }

        return response.code
    }
}
