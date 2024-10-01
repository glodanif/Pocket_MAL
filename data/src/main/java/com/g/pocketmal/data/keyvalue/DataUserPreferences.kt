package com.g.pocketmal.data.keyvalue

data class DataUserPreferences(
    val externalLinkPattern: String
) {

    companion object {
        val defaultPreferences: DataUserPreferences
            get() = DataUserPreferences(externalLinkPattern = "")
    }
}