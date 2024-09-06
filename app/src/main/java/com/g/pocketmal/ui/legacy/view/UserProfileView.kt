package com.g.pocketmal.ui.legacy.view

interface UserProfileView: com.g.pocketmal.ui.legacy.view.BaseSessionView {
    fun displayUserInfo(userInfo: com.g.pocketmal.ui.legacy.viewmodel.UserProfileViewModel)
    fun showFailNotification()
    fun showProgress()
    fun hideProgress()
}