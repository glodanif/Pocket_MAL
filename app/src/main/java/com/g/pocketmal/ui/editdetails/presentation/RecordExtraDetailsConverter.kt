package com.g.pocketmal.ui.editdetails.presentation

import android.content.Context
import com.g.pocketmal.domain.entity.ListRecordEntity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RecordExtraDetailsConverter(
    private val context: Context,
) {

    private val viewFormatter: DateFormat =
        SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    private val dateFormatter: DateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    fun transform(record: ListRecordEntity): RecordExtraDetailsViewEntity {
        return RecordExtraDetailsViewEntity(
            startDate = parseDate(record.myStartDate),
            startDateFormatted = transformDate(record.myStartDate),
            finishDate = parseDate(record.myFinishDate),
            finishDateFormatted = transformDate(record.myFinishDate)
        )
    }

    private fun transformDate(date: String?): String? {
        if (date == null) {
            return null
        }
        val timestamp = dateFormatter.parse(date)?.time
        return if (timestamp == null) null else viewFormatter.format(timestamp)
    }

    private fun parseDate(date: String?): Date? {
        return if (date == null) {
            null
        } else {
            dateFormatter.parse(date)
        }
    }
}
