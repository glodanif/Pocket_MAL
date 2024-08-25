package com.g.pocketmal.domain.entity.converter

import com.g.pocketmal.data.api.request.AuthUrl
import com.g.pocketmal.data.api.response.BasicUserResponse
import com.g.pocketmal.data.api.response.TokenResponse
import com.g.pocketmal.domain.entity.AuthUrlEntity
import com.g.pocketmal.domain.entity.BaseUserEntity
import com.g.pocketmal.domain.entity.TokenEntity

class LoginEntityConverter {

    fun transform(response: TokenResponse): TokenEntity {
        return TokenEntity(response.tokenType, response.expiresIn, response.accessToken, response.refreshToken)
    }

    fun transform(response: BasicUserResponse): BaseUserEntity {
        return BaseUserEntity(response.id, response.name, response.picture ?: "<empty>")
    }

    fun transform(response: AuthUrl): AuthUrlEntity {
        return AuthUrlEntity(response.url, response.codeVerifier, response.redirectUrl)
    }
}