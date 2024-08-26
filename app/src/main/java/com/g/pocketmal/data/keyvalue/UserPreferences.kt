package com.g.pocketmal.data.keyvalue

data class UserPreferences(
    val externalLinkPattern: String
) {

    companion object {
        val defaultPreferences: UserPreferences
            get() = UserPreferences(externalLinkPattern = "")
    }
}
