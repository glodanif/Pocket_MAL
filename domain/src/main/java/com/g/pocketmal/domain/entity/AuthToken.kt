package com.g.pocketmal.domain.entity

data class AuthToken(
    val tokenType: String,
    val expiresIn: Long,
    val accessToken: String,
    val refreshToken: String,
)
