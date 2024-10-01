package com.g.pocketmal.ui.common.inliststatus

import androidx.compose.ui.graphics.Color
import com.g.pocketmal.domain.InListStatus
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.theme.completedBlend
import com.g.pocketmal.ui.theme.droppedBlend
import com.g.pocketmal.ui.theme.inProgressBlend
import com.g.pocketmal.ui.theme.onHoldBlend
import com.g.pocketmal.ui.theme.plannedBlend

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
            InListStatus.IN_PROGRESS -> inProgressBlend
            InListStatus.COMPLETED -> completedBlend
            InListStatus.ON_HOLD -> onHoldBlend
            InListStatus.DROPPED -> droppedBlend
            InListStatus.PLANNED -> plannedBlend
        }

        val scoreLabel = if ((myScore ?: 0) > 0) " • ${myScore}/10" else ""

        return InListStatusViewEntity(
            statusLabel = statusLabel + scoreLabel,
            color = statusColor,
            isInMyList = myListStatus != InListStatus.NOT_IN_LIST,
        )
    }
}
