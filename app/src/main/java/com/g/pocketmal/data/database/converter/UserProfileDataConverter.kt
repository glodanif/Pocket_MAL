package com.g.pocketmal.data.database.converter

import com.g.pocketmal.data.api.response.UserResponse
import com.g.pocketmal.data.database.model.UserProfileTable
import com.g.pocketmal.data.common.ListCounts
import java.text.SimpleDateFormat

class UserProfileDataConverter {

    private val detailedFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

    fun transform(profile: UserResponse): UserProfileTable {

        val animeStatistics = profile.animeStatistics
        val animeWatching = animeStatistics?.numItemsWatching ?: 0
        val animeCompleted = animeStatistics?.numItemsCompleted ?: 0
        val animeOnHold = animeStatistics?.numItemsOnHold ?: 0
        val animeDropped = animeStatistics?.numItemsDropped ?: 0
        val animePlanToWatch = animeStatistics?.numItemsPlanToWatch ?: 0
        val animeCounts = ListCounts(animeWatching, animeCompleted, animeOnHold, animeDropped, animePlanToWatch)

        val animeDays = animeStatistics?.numDays ?: 0.toFloat()
        val animeScore = animeStatistics?.meanScore ?: 0.toFloat()
        val animeEpisodes = animeStatistics?.numEpisodes ?: 0
        val animeRewatched = animeStatistics?.numTimesRewatched ?: 0

        val userJoinedAt = detailedFormatter.parse(profile.joinedAt)?.time ?: 0

        return UserProfileTable(
                profile.id,
                profile.name,
                profile.picture,
                profile.gender,
                profile.birthday,
                profile.location,
                userJoinedAt,
                animeDays,
                animeCounts,
                animeScore,
                animeEpisodes,
                animeRewatched,
                null,
                0.toFloat(),
                ListCounts(0, 0, 0, 0, 0),
                0.toFloat(),
                0,
                0,
                0,
                null,
                profile.timeZone,
                profile.isSupporter ?: false
        )
    }
}