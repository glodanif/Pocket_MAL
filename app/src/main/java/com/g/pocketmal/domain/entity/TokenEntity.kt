package com.g.pocketmal.domain.entity

data class TokenEntity(
        val tokenType: String,
        val expiresIn: Long,
        val accessToken: String,
        val refreshToken: String
)
