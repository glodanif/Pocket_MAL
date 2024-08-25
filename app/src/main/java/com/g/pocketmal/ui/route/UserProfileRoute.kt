package com.g.pocketmal.ui.route

interface UserProfileRoute: BaseSessionRoute {
    fun openUserImage(url: String)
    fun openOnMal(url: String)
}