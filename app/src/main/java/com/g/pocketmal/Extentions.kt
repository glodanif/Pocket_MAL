package com.g.pocketmal

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Int.ordinal(): String {
    val suffixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
    return when (this % 100) {
        11, 12, 13 -> "${this}th"
        else -> "$this${suffixes[this % 10]}"
    }
}

private val VIEW_FORMAT = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

fun Date.getTimePeriod(): String {
    var timestamp = this.time
    if (timestamp < 1000000000000L) {
        timestamp *= 1000
    }
    val now = System.currentTimeMillis()
    if (timestamp > now || timestamp <= 0) {
        return "?"
    }
    val diff = now - timestamp
    return when {
        diff < MINUTE_MILLIS -> {
            "just now"
        }
        diff < 2 * MINUTE_MILLIS -> {
            "a minute ago"
        }
        diff < 50 * MINUTE_MILLIS -> {
            "${diff / MINUTE_MILLIS} minutes ago"
        }
        diff < 90 * MINUTE_MILLIS -> {
            "an hour ago"
        }
        diff < 24 * HOUR_MILLIS -> {
            "${diff / HOUR_MILLIS} hours ago"
        }
        diff < 48 * HOUR_MILLIS -> {
            "yesterday"
        }
        diff < 7 * DAY_MILLIS -> {
            "${diff / DAY_MILLIS} days ago"
        }
        else -> {
            VIEW_FORMAT.format(Date(timestamp))
        }
    }
}

private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
private val shortDateFormatter = SimpleDateFormat("yyyy-MM", Locale.ENGLISH)
private val fullViewFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
private val shortViewFormat = SimpleDateFormat("MMM, yyyy", Locale.ENGLISH)

fun String?.reformatToViewableDate(unknownLabel: String = "?"): String {
    return when {
        this == null -> unknownLabel
        this.length == 4 -> this
        this.length == 7 -> {
            val parsed = shortDateFormatter.parse(this)
            if (parsed == null) unknownLabel else shortViewFormat.format(parsed)
        }
        this.length == 10 -> {
            val parsed = dateFormatter.parse(this)
            if (parsed == null) unknownLabel else fullViewFormat.format(parsed)
        }
        else -> unknownLabel
    }
}

private val numberFormat = NumberFormat.getNumberInstance(Locale.US)

fun Int.formatToSeparatedText(): String {
    return try {
        numberFormat.format(this.toLong())
    } catch (e: NumberFormatException) {
        this.toString()
    }
}

private val decimal = DecimalFormat("0.00")

fun Float.formatToDecimalText(): String = decimal.format(this)
