package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.database.converter.UserProfileDataConverter
import com.g.pocketmal.data.database.datasource.UserProfileDataSource
import com.g.pocketmal.domain.entity.UserProfileEntity
import com.g.pocketmal.data.converter.UserProfileEntityConverter
import com.g.pocketmal.domain.exception.EmptyResponseException

class GetUserProfileInteractor(
        private val apiService: ApiService,
        private val storage: UserProfileDataSource,
        private val dataConverter: UserProfileDataConverter,
        private val converter: UserProfileEntityConverter
) : BaseCachedNetworkCallInteractor<Int, UserProfileEntity>() {

    override suspend fun executeCache(input: Int): UserProfileEntity {

        val table = storage.getUserProfileById(input)

        if (table != null) {
            return converter.transform(table)
        } else {
            throw EmptyResponseException("No user was found with id=$input in db")
        }
    }

    override suspend fun executeNetwork(input: Int): UserProfileEntity {

        val response = apiService.getMyUserInfo()

        val profileResponse = response.body()
        if (response.isSuccessful && profileResponse != null) {
            val dbRecord = dataConverter.transform(profileResponse)
            storage.saveUserProfile(dbRecord)
            return converter.transform(dbRecord)
        } else {
            throw NetworkException(response.code(), errorMessage = "Getting user profile request was not successful")
        }
    }
}