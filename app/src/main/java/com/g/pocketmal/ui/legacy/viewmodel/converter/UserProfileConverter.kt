package com.g.pocketmal.ui.legacy.viewmodel.converter

import com.g.pocketmal.R
import com.g.pocketmal.data.api.MalApi
import com.g.pocketmal.domain.entity.UserProfileEntity
import com.g.pocketmal.formatToDecimalText
import com.g.pocketmal.formatToSeparatedText
import com.g.pocketmal.reformatToViewableDate
import java.text.SimpleDateFormat
import java.util.*

class UserProfileConverter {

    private val viewFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

    fun transform(entity: UserProfileEntity): com.g.pocketmal.ui.legacy.viewmodel.UserProfileViewModel {

        val gender = entity.gender
        val isNormalGender = gender != null && gender.isNotEmpty() &&
                ("female".equals(gender, ignoreCase = true) || "male".equals(gender, ignoreCase = true))
        val isFemale = gender != null && gender.isNotEmpty() && isNormalGender && "female".equals(gender, ignoreCase = true)
        val genderDrawable = if (isFemale) R.drawable.ic_female else R.drawable.ic_male

        val location = entity.location
        val birthday = entity.birthday
        val isBirthdayAvailable = birthday != null && birthday.isNotEmpty()
        val isLocationAvailable = location != null && location.isNotEmpty()

        return com.g.pocketmal.ui.legacy.viewmodel.UserProfileViewModel(
            entity.id,
            entity.name,
            entity.avatar,
            genderDrawable,
            entity.birthday.reformatToViewableDate(),
            entity.location ?: "",
            viewFormat.format(entity.joinDate),
            entity.isSupporter,
            String.format("%s/profile/%s", MalApi.MAL_HOST, entity.name),
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
            isNormalGender,
            isLocationAvailable,
            isBirthdayAvailable
        )
    }
}
