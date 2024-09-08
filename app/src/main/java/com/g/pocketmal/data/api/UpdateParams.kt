package com.g.pocketmal.data.api

import android.text.TextUtils
import java.io.Serializable

data class UpdateParams(
    var episodes: Int? = null,
    var chapters: Int? = null,
    var volumes: Int? = null,
    var status: String? = null,
    var score: Int? = null,
    var tags: StringableList? = null,
    var startDate: String? = null,
    var finishDate: String? = null,
    var reWatching: Boolean? = null,
    var reWatchedTimes: Int? = null,
    var reWatchedValue: Int? = null,
    var reReading: Boolean? = null,
    var reReadTimes: Int? = null,
    var reReadValue: Int? = null,
    var comments: String? = null,
    var priority: Int? = null,
) : Serializable {

    class StringableList: ArrayList<String>()   {

        override fun toString(): String {
            return TextUtils.join(", ", this)
        }
    }
}
