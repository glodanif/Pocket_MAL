package com.g.pocketmal.data.converter

import com.g.pocketmal.data.database.model.UserProfileTable
import com.g.pocketmal.domain.entity.UserProfile

class UserProfileEntityConverter {

    fun transform(response: UserProfileTable): UserProfile {

        return UserProfile(
            response.id,
            response.name,
            response.avatar,
            response.gender,
            response.birthday,
            response.location,
            response.joinDate,
            response.isSupporter,
            response.animeSpentDays,
            response.animeCounts,
            response.animeMeanScore,
            response.animeEpisodes,
            response.animeRewatched,
            response.mangaSpentDays,
            response.mangaCounts,
            response.mangaMeanScore,
            response.mangaChapters,
            response.mangaVolumes,
            response.mangaReread,
        )
    }
}