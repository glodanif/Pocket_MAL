package com.g.pocketmal.ui.common.inliststatus

import androidx.compose.ui.graphics.Color
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.InListStatus

class InListStatusConverter {

    fun transform(
        myListStatus: InListStatus,
        myScore: Int?,
        titleType: TitleType,
    ): InListStatusViewEntity {
        val statusLabel = when (myListStatus) {
            InListStatus.NOT_IN_LIST -> ""
            InListStatus.IN_PROGRESS -> if (titleType.isAnime()) "Watching" else "Reading"
            InListStatus.COMPLETED -> "Completed"
            InListStatus.ON_HOLD -> "On Hold"
            InListStatus.DROPPED -> "Dropped"
            InListStatus.PLANNED -> if (titleType.isAnime()) "Plan to Watch" else "Plan to Read"
        }
        val statusColor = when (myListStatus) {
            InListStatus.NOT_IN_LIST -> Color.Transparent
            InListStatus.IN_PROGRESS -> Color(0xff338543)
            InListStatus.COMPLETED -> Color(0xff2d4276)
            InListStatus.ON_HOLD -> Color(0xffc9a31f)
            InListStatus.DROPPED -> Color(0xff832f30)
            InListStatus.PLANNED -> Color(0xff747474)
        }

        val scoreLabel = if ((myScore ?: 0) > 0) " • ${myScore}/10" else ""

        return InListStatusViewEntity(
            statusLabel = statusLabel + scoreLabel,
            color = statusColor,
            isInMyList = myListStatus != InListStatus.NOT_IN_LIST,
        )
    }
}
