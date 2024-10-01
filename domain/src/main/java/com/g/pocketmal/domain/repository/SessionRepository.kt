package com.g.pocketmal.domain.repository

import com.g.pocketmal.domain.entity.AuthUrl
import com.g.pocketmal.domain.entity.BaseUser

interface SessionRepository {
    fun isUserLoggedIn(): Boolean
    fun getCurrentUser(): BaseUser?
    fun generateAuthData(): AuthUrl
    suspend fun authorize(code: String, verifier: String)
    suspend fun logout()
}
