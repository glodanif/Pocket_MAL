package com.g.pocketmal.domain.repository

import com.g.pocketmal.domain.result.UserProfileResult

interface UserProfileRepository {
    suspend fun getUserProfileFromLocalStorage(): UserProfileResult
    suspend fun getUserProfileFromNetwork(): UserProfileResult
    suspend fun logout()
}
