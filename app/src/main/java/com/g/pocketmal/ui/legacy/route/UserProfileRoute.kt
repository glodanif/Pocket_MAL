package com.g.pocketmal.ui.legacy.route

interface UserProfileRoute: com.g.pocketmal.ui.legacy.route.BaseSessionRoute {
    fun openUserImage(url: String)
    fun openOnMal(url: String)
}