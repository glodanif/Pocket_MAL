package com.g.pocketmal.data.api

import android.text.TextUtils
import java.io.Serializable

class UpdateParams(
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
        var reReading: Boolean? = null,
        var reReadTimes: Int? = null
) : Serializable {

    override fun toString(): String {
        return "UpdateParams(episodes=$episodes, chapters=$chapters, volumes=$volumes, status=$status, score=$score, tags=$tags, startDate=$startDate, finishDate=$finishDate, reWatching=$reWatching, reWatchedTimes=$reWatchedTimes, reReading=$reReading, reReadTimes=$reReadTimes)"
    }

    class StringableList: ArrayList<String>()   {

        override fun toString(): String {
            return TextUtils.join(", ", this)
        }
    }
}
