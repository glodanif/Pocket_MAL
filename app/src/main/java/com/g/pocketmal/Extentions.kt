package com.g.pocketmal

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.g.pocketmal.ui.utils.MosaicTransform
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private val bbMap: MutableMap<String, String> = hashMapOf(
        "(\r\n|\r|\n|\n\r)" to "<br/>",
        "\\[b\\](.+?)\\[/b\\]" to "<strong>$1</strong>",
        "\\[i\\](.+?)\\[/i\\]" to "<span style='font-style:italic;'>$1</span>",
        "\\[u\\](.+?)\\[/u\\]" to "<span style='text-decoration:underline;'>$1</span>",
        "\\[h1\\](.+?)\\[/h1\\]" to "<h1>$1</h1>",
        "\\[h2\\](.+?)\\[/h2\\]" to "<h2>$1</h2>",
        "\\[h3\\](.+?)\\[/h3\\]" to "<h3>$1</h3>",
        "\\[h4\\](.+?)\\[/h4\\]" to "<h4>$1</h4>",
        "\\[h5\\](.+?)\\[/h5\\]" to "<h5>$1</h5>",
        "\\[h6\\](.+?)\\[/h6\\]" to "<h6>$1</h6>",
        "\\[quote\\](.+?)\\[/quote\\]" to "<blockquote>$1</blockquote>",
        "\\[p\\](.+?)\\[/p\\]" to "<p>$1</p>",
        "\\[p=(.+?),(.+?)\\](.+?)\\[/p\\]" to "<p style='text-indent:$1px;line-height:$2%;'>$3</p>",
        "\\[color=(.+?)\\](.+?)\\[/color\\]" to "<span style='color:$1;'>$2</span>",
        "\\[size=(.+?)\\](.+?)\\[/size\\]" to "<span style='font-size:$1;'>$2</span>",
        "\\[email\\](.+?)\\[/email\\]" to "<a href='mailto:$1'>$1</a>",
        "\\[email=(.+?)\\](.+?)\\[/email\\]" to "<a href='mailto:$1'>$2</a>",
        "\\[url\\](.+?)\\[/url\\]" to "<a href='$1'>$1</a>",
        "\\[url=(.+?)\\](.+?)\\[/url\\]" to "<a href='$1'>$2</a>"
)

fun <T : View> Activity.bind(@IdRes idRes: Int) =
        lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }

fun <T : View> bind(root: View, @IdRes idRes: Int) =
        lazy(LazyThreadSafetyMode.NONE) { root.findViewById<T>(idRes) }

inline fun <reified T : Any?> AppCompatActivity.argument(key: String) = lazy {
    intent.extras?.let {
        return@lazy it[key] as T
    }
    return@lazy null
}

inline fun <reified T : Any> AppCompatActivity.argument(key: String, defaultValue: T) = lazy {
    intent.extras?.let {
        return@lazy it[key] as? T ?: defaultValue
    }
    return@lazy defaultValue
}

inline fun <reified T : Any, reified V : Any> AppCompatActivity.transformedArgument(key: String, defaultValue: V, noinline converter: (T) -> V) = lazy {
    intent.extras?.let {
        val extra = it[key] as? T
        return@lazy if (extra == null) defaultValue else converter.invoke(extra)
    }
    return@lazy defaultValue
}

inline fun <reified T : Any, reified V : Any> Fragment.transformedArgument(key: String, defaultValue: V, noinline converter: (T) -> V) = lazy {
    arguments?.let {
        val extra = it[key] as? T
        return@lazy if (extra == null) defaultValue else converter.invoke(extra)
    }
    return@lazy defaultValue
}

fun Int.ordinal(): String {
    val suffixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
    return when (this % 100) {
        11, 12, 13 -> "${this}th"
        else -> "$this${suffixes[this % 10]}"
    }
}

fun String.bb(): String {
    var html = this
    for ((key, value) in bbMap) {
        html = html.replace(key.toRegex(), value)
    }
    return html
}

fun EditText.getTrimmedText(): String {
    return this.text.toString().trim { it <= ' ' }
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

@ColorInt
fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    val theme: Resources.Theme = theme
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
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

fun ImageView.loadUrl(url: String?, @DrawableRes placeholder: Int = R.color.poster_background, transformation: Transformation? = null) {

    var imageUrl = url
    if (url.isNullOrEmpty()) {
        imageUrl = "<placeholder>"
    }

    var creator = Picasso.get()
            .load(imageUrl)
            .error(placeholder)
            .placeholder(placeholder)

    if (transformation != null) {
        creator = creator.transform(transformation)
    }

    if (BuildConfig.SCREENSHOTS) {
        creator = creator.transform(MosaicTransform())
    }
    creator.into(this)
}
