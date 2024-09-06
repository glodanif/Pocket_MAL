package com.g.pocketmal.ui.legacy.viewmodel.converter

import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import com.g.pocketmal.domain.entity.SeasonEntity
import java.text.DecimalFormat

class SeasonalAnimeConverter {

    private val membersFormatter = DecimalFormat("#,###,###")
    private val scoreFormatter = DecimalFormat("0.00")
    private val sizeSpan = RelativeSizeSpan(.6f)

    fun transform(item: SeasonEntity): com.g.pocketmal.ui.legacy.viewmodel.SeasonalAnimeViewModel {

        val broadcast = item.broadcast
        val day = if (broadcast == null)
            "?" else
            broadcast.dayOfTheWeek.substring(0, 1).toUpperCase() + broadcast.dayOfTheWeek.substring(1)
        val time = if (broadcast?.startTime != null) broadcast.startTime else "?"
        val genres = item.genres.map { it.name }
        val genresLabel = TextUtils.join(", ", genres)
        val source = when (item.source) {
            "other" -> "Other"
            "original" -> "Original"
            "manga" -> "Manga"
            "4_koma_manga" -> "4 Koma Manga"
            "web_manga" -> "Web Manga"
            "digital_manga" -> "Digital Manga"
            "novel" -> "Novel"
            "light_novel" -> "Light Novel"
            "visual_novel" -> "Visual Novel"
            "game" -> "Game"
            "card_game" -> "Card Game"
            "book" -> "Book"
            "picture_book" -> "Picture Book"
            "radio" -> "Radio"
            "music" -> "Music"
            else -> "Unknown"
        }

        val score = if (item.score != null) {
            val spannable: Spannable = SpannableString(scoreFormatter.format(item.score.toFloat()))
            spannable.setSpan(sizeSpan, 1, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable
        } else {
            SpannableString("—")
        }

        val episodes = (if (item.episodes == 0) "?" else item.episodes.toString()) + " eps"
        val infoList = mutableListOf(episodes, source)
        if (item.studios != null && item.studios.isNotEmpty()) {
            infoList.add(item.studios[0].name)
        }
        val info = TextUtils.join(" • ", infoList)

        return com.g.pocketmal.ui.legacy.viewmodel.SeasonalAnimeViewModel(
            item.id,
            item.title,
            item.picture?.large,
            info,
            genresLabel,
            Html.fromHtml(item.synopsis).toString(),
            "$day ($time)",
            membersFormatter.format(item.listUsers),
            score
        )
    }

    fun transform(items: List<SeasonEntity>): List<com.g.pocketmal.ui.legacy.viewmodel.SeasonalAnimeViewModel> {
        return items.map { transform(it) }
    }
}
