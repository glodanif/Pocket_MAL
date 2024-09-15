package com.g.pocketmal.ui.userprofile.presentation

sealed class UserProfileState {
    data object Loading : UserProfileState()
    data class UserProfileLoaded(val userProfile: UserProfileViewEntity) : UserProfileState()
    data class FailedToLoad(val message: String?) : UserProfileState()
    data object LoggedOut : UserProfileState()
}
