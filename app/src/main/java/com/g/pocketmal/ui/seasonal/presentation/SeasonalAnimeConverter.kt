package com.g.pocketmal.ui.seasonal.presentation

import android.content.Context
import android.text.Html
import com.g.pocketmal.R
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.entity.SeasonEntity
import com.g.pocketmal.ui.common.inliststatus.InListStatusConverter
import java.text.DecimalFormat

class SeasonalAnimeConverter(
    private val context: Context,
    private val statusConverter: InListStatusConverter,
) {

    private val membersFormatter = DecimalFormat("#,###,###")

    fun transform(item: SeasonEntity): SeasonalAnimeViewEntity {

        val broadcast = item.broadcast
        val day = if (broadcast != null)
            broadcast.dayOfTheWeek.substring(0, 1).uppercase() + broadcast.dayOfTheWeek.substring(1)
        else
            null
        val time = if (broadcast?.startTime != null) " (${broadcast.startTime})" else null
        val airing = if (day == null) null else "$day ${time ?: ""}"

        val genres = item.genres.map { it.name }
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
            else -> null
        }

        val episodes = if (item.episodes == 0) null else
            context.resources.getQuantityString(
                R.plurals.shortEpisodes,
                item.episodes,
                item.episodes
            )
        val studio = if (!item.studios.isNullOrEmpty()) item.studios[0].name else null

        val inListStatus =
            statusConverter.transform(item.myListStatus, item.myScore, TitleType.ANIME)

        return SeasonalAnimeViewEntity(
            id = item.id,
            title = item.title,
            poster = item.picture?.large,
            source = source,
            studio = studio,
            genres = genres,
            episodes = episodes,
            synopsis = Html.fromHtml(item.synopsis).toString(),
            airing = airing,
            members = membersFormatter.format(item.listUsers),
            score = if (item.score != null) item.score.toString() else "—",
            inListStatus = inListStatus,
        )
    }

    fun transform(items: List<SeasonEntity>): List<SeasonalAnimeViewEntity> {
        return items.map { transform(it) }
    }
}
