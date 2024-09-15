package com.g.pocketmal.ui.userprofile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g.pocketmal.data.repository.UserProfileRepository
import com.g.pocketmal.data.repository.UserProfileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: UserProfileRepository,
    private val converter: UserProfileConverter,
) : ViewModel() {

    private val _userProfileState = MutableStateFlow<UserProfileState>(UserProfileState.Loading)
    val userProfileState = _userProfileState.asStateFlow()

    fun loadUserProfileFromDb(userId: Int) {
        _userProfileState.value = UserProfileState.Loading
        viewModelScope.launch {
            val dbResult = repository.getUserProfileFromLocalStorage(userId)
            if (dbResult is UserProfileResult.Result) {
                val userProfile = converter.transform(dbResult.userProfile)
                _userProfileState.value = UserProfileState.UserProfileLoaded(userProfile)
            }

            val networkResult = repository.getUserProfileFromNetwork()
            if (networkResult is UserProfileResult.Result) {
                val userProfile = converter.transform(networkResult.userProfile)
                _userProfileState.value = UserProfileState.UserProfileLoaded(userProfile)
            } else if (networkResult is UserProfileResult.Error && dbResult !is UserProfileResult.Result) {
                _userProfileState.value =
                    UserProfileState.FailedToLoad(message = networkResult.throwable.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _userProfileState.value = UserProfileState.LoggedOut
        }
    }
}
