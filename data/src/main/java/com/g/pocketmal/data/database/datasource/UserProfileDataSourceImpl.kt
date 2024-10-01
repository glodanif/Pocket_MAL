package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.model.UserProfileTable

class UserProfileDataSourceImpl(private val listDbStorage: ListDbStorage) : UserProfileDataSource {

    private val userProfileDao = listDbStorage.db.userDao()

    override suspend fun getUserProfileById(userId: Int): UserProfileTable? {
        return userProfileDao.getUserById(userId)
    }

    override suspend fun saveUserProfile(userProfile: UserProfileTable) {
        userProfileDao.insert(userProfile)
    }
}
