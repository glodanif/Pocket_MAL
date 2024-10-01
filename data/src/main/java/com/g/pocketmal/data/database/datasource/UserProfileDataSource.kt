package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.database.model.UserProfileTable

interface UserProfileDataSource {
    suspend fun getUserProfileById(userId: Int): UserProfileTable?
    suspend fun saveUserProfile(userProfile: UserProfileTable)
}
