package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.platform.CookieManager
import com.g.pocketmal.data.platform.NetworkManager
import com.g.pocketmal.domain.entity.AuthUrlEntity
import com.g.pocketmal.domain.entity.converter.LoginEntityConverter
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class LoadLoginPageInteractor(
        private val apiService: ApiService,
        private val networkManager: NetworkManager,
        private val cookieManager: CookieManager,
        private val converter: LoginEntityConverter
) : BaseInteractor<Unit, AuthUrlEntity>(executionContext = Dispatchers.Default) {

    override suspend fun execute(input: Unit): AuthUrlEntity {

        cookieManager.clearCookies()

        val authUrl = apiService.getAuthUrl()

        if (networkManager.isNetworkAvailable()) {
            return converter.transform(authUrl)
        } else {
            throw IOException("No internet connection")
        }
    }
}