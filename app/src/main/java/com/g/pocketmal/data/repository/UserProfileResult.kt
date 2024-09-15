package com.g.pocketmal.data.repository

import com.g.pocketmal.domain.entity.UserProfileEntity

sealed class UserProfileResult {
    data class Result(val userProfile: UserProfileEntity) : UserProfileResult()
    data object EmptyResult : UserProfileResult()
    data class Error(val throwable: Throwable) : UserProfileResult()
}
