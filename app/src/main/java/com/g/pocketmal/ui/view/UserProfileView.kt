package com.g.pocketmal.ui.view

import com.g.pocketmal.ui.viewmodel.UserProfileViewModel

interface UserProfileView: BaseSessionView {
    fun displayUserInfo(userInfo: UserProfileViewModel)
    fun showFailNotification()
    fun showProgress()
    fun hideProgress()
}