package com.g.pocketmal.domain

data class UpdateParameters(
    var episodes: Int? = null,
    var chapters: Int? = null,
    var volumes: Int? = null,
    var status: InListStatus? = null,
    var score: Int? = null,
    var tags: List<String>? = null,
    var startDate: String? = null,
    var finishDate: String? = null,
    var reWatching: Boolean? = null,
    var reWatchedTimes: Int? = null,
    var reWatchValue: Int? = null,
    var reReading: Boolean? = null,
    var reReadTimes: Int? = null,
    var reReadValue: Int? = null,
    var comments: String? = null,
    var priority: Int? = null,
)

const val UNSET_DATE = "0000-00-00"
