package com.g.pocketmal.ui.editdetails.presentation

import android.content.Context
import com.g.pocketmal.data.common.Status
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
            titleType = record.titleType,
            startDate = domainToDate(record.myStartDate),
            startDateFormatted = domainToViewDate(record.myStartDate),
            finishDate = domainToDate(record.myFinishDate),
            finishDateFormatted = domainToViewDate(record.myFinishDate),
            isReAvailable = record.myStatus == Status.COMPLETED || record.myRe,
            isRe = record.myRe,
            reTimes = record.myReTimes,
            reValue = record.myReValue,
            comments = record.myComments,
            priority = record.myPriority,
        )
    }

    private fun domainToViewDate(date: String?): String? {
        if (date == null) {
            return null
        }
        val timestamp = dateFormatter.parse(date)?.time
        return if (timestamp == null) null else viewFormatter.format(timestamp)
    }

    private fun domainToDate(date: String?): Date? {
        return if (date == null) {
            null
        } else {
            dateFormatter.parse(date)
        }
    }

    internal fun timestampToDomainDate(timestamp: Long?): String? {
        return if (timestamp == null) {
            null
        } else {
            dateFormatter.format(Date(timestamp))
        }
    }

    internal fun timestampToViewDate(timestamp: Long?): String? {
        return if (timestamp == null) {
            null
        } else {
            viewFormatter.format(Date(timestamp))
        }
    }
}
