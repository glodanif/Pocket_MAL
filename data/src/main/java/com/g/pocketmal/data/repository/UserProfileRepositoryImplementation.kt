package com.g.pocketmal.data.repository

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.api.SessionExpiredException
import com.g.pocketmal.data.converter.UserProfileEntityConverter
import com.g.pocketmal.data.database.converter.UserProfileDataConverter
import com.g.pocketmal.data.database.datasource.UserProfileDataSource
import com.g.pocketmal.domain.repository.SessionRepository
import com.g.pocketmal.domain.repository.UserProfileRepository
import com.g.pocketmal.domain.result.UserProfileResult

class UserProfileRepositoryImplementation(
    private val apiService: ApiService,
    private val storage: UserProfileDataSource,
    private val dataConverter: UserProfileDataConverter,
    private val converter: UserProfileEntityConverter,
    private val sessionRepository: SessionRepository,
) : UserProfileRepository {

    override suspend fun getUserProfileFromLocalStorage(): UserProfileResult {
        val user = sessionRepository.getCurrentUser()
        if (user != null) {
            val table = storage.getUserProfileById(user.id)
            if (table != null) {
                val userProfile = converter.transform(table)
                return UserProfileResult.Result(userProfile)
            } else {
                return UserProfileResult.EmptyResult
            }
        } else {
            return UserProfileResult.Error(SessionExpiredException())
        }
    }

    override suspend fun getUserProfileFromNetwork(): UserProfileResult {
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

    override suspend fun logout() {
        sessionRepository.logout()
    }
}
