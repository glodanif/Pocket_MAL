package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class ListStatus(
        val status: String,
        val score: Int,
        @SerializedName("num_episodes_watched")
        val numEpisodesWatched: Int,
        @SerializedName("num_chapters_read")
        val numChaptersRead: Int,
        @SerializedName("num_volumes_read")
        val numVolumesRead: Int,
        @SerializedName("is_rewatching")
        val isRewatching: Boolean,
        @SerializedName("is_rereading")
        val isRereading: Boolean,
        @SerializedName("num_times_reread")
        val numTimesReread: Int,
        @SerializedName("num_times_rewatched")
        val numTimesRewatch: Int,
        @SerializedName("reread_value")
        val rereadValue: Int,
        @SerializedName("rewatch_value")
        val rewatchValue: Int,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("start_date")
        val startDate: String?,
        @SerializedName("finish_date")
        val finishDate: String?,
        @SerializedName("comments")
        val comments: String,
        @SerializedName("tags")
        val tags: List<String>,
        @SerializedName("priority")
        val priority: Int,
)
