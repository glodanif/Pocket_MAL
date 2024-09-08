package com.g.pocketmal.ui.editdetails.presentation

import com.g.pocketmal.data.util.TitleType
import java.util.Date

data class RecordExtraDetailsViewEntity(
    val titleType: TitleType,
    val startDate: Date?,
    val startDateFormatted: String?,
    val finishDate: Date?,
    val finishDateFormatted: String?,
    val isReAvailable: Boolean,
    val isRe: Boolean,
    val reTimes: Int,
    val reValue: Int,
    val comments: String?,
    val priority: Int,
)
