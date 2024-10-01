package com.g.pocketmal.domain.result

import com.g.pocketmal.domain.entity.UserProfile

sealed class UserProfileResult {
    data class Result(val userProfile: UserProfile) : UserProfileResult()
    data object EmptyResult : UserProfileResult()
    data class Error(val throwable: Throwable) : UserProfileResult()
}
