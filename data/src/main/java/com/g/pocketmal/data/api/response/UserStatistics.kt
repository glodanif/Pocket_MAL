package com.g.pocketmal.data.api.response

import com.google.gson.annotations.SerializedName

data class UserStatistics(
        @SerializedName("num_items_watching")
        val numItemsWatching: Int,
        @SerializedName("num_items_completed")
        val numItemsCompleted: Int,
        @SerializedName("num_items_on_hold")
        val numItemsOnHold: Int,
        @SerializedName("num_items_dropped")
        val numItemsDropped: Int,
        @SerializedName("num_items_plan_to_watch")
        val numItemsPlanToWatch: Int,
        @SerializedName("num_items")
        val numItems: Int,
        @SerializedName("num_days_watched")
        val numDaysWatched: Float,
        @SerializedName("num_days_watching")
        val numDaysWatching: Float,
        @SerializedName("num_days_completed")
        val numDaysCompleted: Float,
        @SerializedName("num_days_on_hold")
        val numDaysOnHold: Float,
        @SerializedName("num_days_dropped")
        val numDaysDropped: Float,
        @SerializedName("num_days")
        val numDays: Float,
        @SerializedName("num_watching_days")
        val numWatchingDays: Float,
        @SerializedName("num_episodes")
        val numEpisodes: Int,
        @SerializedName("num_times_rewatched")
        val numTimesRewatched: Int,
        @SerializedName("mean_score")
        val meanScore: Float
)
