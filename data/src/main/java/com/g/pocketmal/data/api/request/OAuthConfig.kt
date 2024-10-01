package com.g.pocketmal.data.api.request

import com.g.pocketmal.BuildConfig

class OAuthConfig {

    val clientId: String = BuildConfig.clientId
    val redirectUrl: String = BuildConfig.redirectUrl

    init {
        require(!(clientId.isEmpty() || redirectUrl.isEmpty())) {
            "API credentials are empty, make sure pocketmal.properties file in your gradle.properties is set up correctly"
        }
    }
}
