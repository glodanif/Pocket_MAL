package com.g.pocketmal.ui.userprofile.presentation

import com.g.pocketmal.domain.MAL_HOST
import com.g.pocketmal.domain.entity.UserProfile
import com.g.pocketmal.formatToDecimalText
import com.g.pocketmal.formatToSeparatedText
import com.g.pocketmal.reformatToViewableDate
import com.g.pocketmal.ui.theme.completedBright
import com.g.pocketmal.ui.theme.droppedBright
import com.g.pocketmal.ui.theme.inProgressBright
import com.g.pocketmal.ui.theme.onHoldBright
import com.g.pocketmal.ui.theme.plannedBright
import java.text.SimpleDateFormat
import java.util.*

class UserProfileConverter {

    private val viewFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

    fun transform(entity: UserProfile): UserProfileViewEntity {

        val gender = entity.gender
        val isTraditionalGender = !gender.isNullOrEmpty() &&
                ("female".equals(gender, ignoreCase = true) || "male".equals(
                    gender,
                    ignoreCase = true
                ))
        val isFemale =
            !gender.isNullOrEmpty() && isTraditionalGender &&
                    "female".equals(gender, ignoreCase = true)

        val location = entity.location
        val birthday = entity.birthday
        val isBirthdayAvailable = !birthday.isNullOrEmpty()
        val isLocationAvailable = !location.isNullOrEmpty()

        val progresses = listOf(
            ProgressItem(entity.animeCounts.inProgressCount, inProgressBright),
            ProgressItem(entity.animeCounts.completedCount, completedBright),
            ProgressItem(entity.animeCounts.onHoldCount, onHoldBright),
            ProgressItem(entity.animeCounts.droppedCount, droppedBright),
            ProgressItem(entity.animeCounts.plannedCount, plannedBright),
        )

        return UserProfileViewEntity(
            entity.id,
            entity.name,
            entity.avatar,
            entity.birthday.reformatToViewableDate(),
            entity.location ?: "",
            viewFormat.format(entity.joinDate),
            entity.isSupporter,
            "${MAL_HOST}profile/${entity.name}",
            entity.animeSpentDays.formatToDecimalText(),
            entity.animeCounts,
            entity.animeMeanScore.formatToDecimalText(),
            entity.animeEpisodes.formatToSeparatedText(),
            entity.animeRewatched,
            entity.mangaSpentDays.formatToDecimalText(),
            entity.mangaCounts,
            entity.mangaMeanScore.formatToDecimalText(),
            entity.mangaChapters.formatToSeparatedText(),
            entity.mangaVolumes.formatToSeparatedText(),
            entity.mangaReread,
            isTraditionalGender,
            isFemale,
            isLocationAvailable,
            isBirthdayAvailable,
            if (isTraditionalGender) gender ?: "" else "",
            progresses,
        )
    }
}
