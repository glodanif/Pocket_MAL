package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.converter.UserProfileEntityConverter
import com.g.pocketmal.data.database.converter.UserProfileDataConverter
import com.g.pocketmal.data.database.datasource.UserProfileDataSource

class UserProfileRepository(
    private val apiService: ApiService,
    private val storage: UserProfileDataSource,
    private val dataConverter: UserProfileDataConverter,
    private val converter: UserProfileEntityConverter,
    private val sessionRepository: SessionRepository,
) {

    suspend fun getUserProfileFromLocalStorage(userId: Int): UserProfileResult {
        val table = storage.getUserProfileById(userId)
        if (table != null) {
            val userProfile = converter.transform(table)
            return UserProfileResult.Result(userProfile)
        } else {
            return UserProfileResult.EmptyResult
        }
    }

    suspend fun getUserProfileFromNetwork(): UserProfileResult {
        val response = apiService.getMyUserInfo()
        val profileResponse = response.body()
        if (response.isSuccessful && profileResponse != null) {
            val dbRecord = dataConverter.transform(profileResponse)
            storage.saveUserProfile(dbRecord)
            val userProfile = converter.transform(dbRecord)
            return UserProfileResult.Result(userProfile)
        } else {
            val error = NetworkException(
                response.code(),
                errorMessage = "Getting user profile request was not successful"
            )
            return UserProfileResult.Error(error)
        }
    }

    suspend fun logout() {
        sessionRepository.logout()
    }
}
