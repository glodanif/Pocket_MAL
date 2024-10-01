package com.g.pocketmal.ui.legacy.viewentity.converter

import android.content.Context
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import androidx.annotation.StringRes
import com.g.pocketmal.R
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.entity.RelatedTitle
import com.g.pocketmal.domain.entity.TitleDetails
import com.g.pocketmal.formatToSeparatedText
import com.g.pocketmal.reformatToViewableDate
import com.g.pocketmal.ui.legacy.viewentity.TitleDetailsViewModel
import com.g.pocketmal.util.list.DataInterpreter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class TitleDetailsConverter(private val context: Context) {

    private var scoreFormat = DecimalFormat("0.00", DecimalFormatSymbols(Locale.US))
    private var sizeSpan = RelativeSizeSpan(.6f)

    fun transform(details: TitleDetails): TitleDetailsViewModel {

        val score = details.score
        val scoreLabel = if (score != null) {
            val spannable: Spannable = SpannableString(scoreFormat.format(score.toFloat()))
            spannable.setSpan(sizeSpan, 1, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable
        } else {
            SpannableString("N/A")
        }

        val synopsis = if (TextUtils.isEmpty(details.synopsis))
            context.getString(R.string.emptySynopsis) else Html.fromHtml(details.synopsis)
            .toString()

        //FIXME move to strings
        val scoredCount = if (details.scoredUsersCount == 1)
            "1 score" else ("${details.scoredUsersCount.formatToSeparatedText()} scores")

        val synonyms = details.synonyms
        val synonymsLabels = if (synonyms == null)
            null else TextUtils.join(", ", synonyms)

        val episodesTypeLabel = context.getString(
            if (details.titleType == TitleType.ANIME) R.string.episodes else R.string.chapters
        )
        val subEpisodesTypeLabel =
            if (details.titleType == TitleType.ANIME) "" else context.getString(R.string.volumes)

        val episodesPlural = episodesTypeLabel.toLowerCase()
            .substring(0, episodesTypeLabel.length - if (details.episodes == 1) 1 else 0)
        val episodesLabel = (if (details.episodes == 0)
            "?" else details.episodes.toString()) + " " + episodesPlural

        val subEpisodesPlural: String = subEpisodesTypeLabel.toLowerCase()
            .substring(0, subEpisodesTypeLabel.length - if (details.subEpisodes == 1) 1 else 0)
        val subEpisodesLabel = (if (details.subEpisodes == 0)
            "?" else details.subEpisodes.toString()) + " " + subEpisodesPlural

        val ranked = details.ranked
        val rankedLabel = if (ranked == null) "N/A" else "#${ranked.formatToSeparatedText()}"

        val screenTitle = context.getString(
            if (details.titleType == TitleType.ANIME) R.string.animeDetailsTitle else R.string.mangaDetailsTitle
        )

        val airingLabel = getAiringLine(details)
        val titleDetails = getDetailsList(details)

        return TitleDetailsViewModel(
            details.id,
            details.titleType,
            details.startDate.reformatToViewableDate(),
            details.finishDate.reformatToViewableDate(),
            DataInterpreter.getMediaTypeLabelFromNetworkConst(details.type),
            DataInterpreter.getSeriesStatusFromNetworkConst(details.status),
            details.episodes,
            details.subEpisodes,
            details.imageUrl,
            synopsis,
            details.title,
            details.englishTitle,
            synonymsLabels,
            details.japaneseTitle,
            scoreLabel,
            scoredCount,
            rankedLabel,
            details.genres,
            getRelatedTitles(details),
            screenTitle,
            titleDetails,
            airingLabel,
            episodesLabel,
            subEpisodesLabel,
            details.titleType == TitleType.MANGA
        )
    }

    fun transform(details: List<TitleDetails>): List<TitleDetailsViewModel> {
        val viewModels: MutableList<TitleDetailsViewModel> = ArrayList()
        for (item in details) {
            viewModels.add(transform(item))
        }
        return viewModels
    }

    @StringRes
    private fun getRatingLabel(rating: String?): Int {

        return when (rating) {
            "g" -> R.string.anime_rating_g
            "pg" -> R.string.anime_rating_pg
            "pg_13" -> R.string.anime_rating_pg13
            "r" -> R.string.anime_rating_r
            "r+" -> R.string.anime_rating_rplus
            "rx" -> R.string.anime_rating_rx
            else -> R.string.unknown
        }
    }

    private fun getAiringLine(details: TitleDetails): String? {

        val airingStats = ArrayList<String>()
        val premiered = details.premiered
        if (premiered != null) {
            airingStats.add(premiered.season.capitalize() + " " + premiered.year)
        }
        val broadcast = details.broadcast
        if (broadcast != null) {
            val time = broadcast.startTime
            val timeLabel = if (time == null) "" else " at $time"
            airingStats.add(broadcast.dayOfTheWeek.capitalize() + "s" + timeLabel)
        }

        //FIXME move to strings
        val duration = details.duration
        if (duration != null) {
            var minutes: Int = duration / 60
            val episodeDuration = when {
                minutes >= 60 -> {
                    val hours = minutes / 60
                    minutes %= 60
                    String.format("%d hr. %d min.", hours % 24, minutes)
                }

                minutes > 0 -> {
                    String.format("%d min. per ep.", minutes)
                }

                else -> {
                    "? min. per ep."
                }
            }
            airingStats.add(episodeDuration)
        }

        return if (airingStats.isNotEmpty()) TextUtils.join(" • ", airingStats) else null
    }

    private fun getDetailsList(details: TitleDetails): List<Pair<String, String>> {

        val titleDetails: MutableList<Pair<String, String>> = ArrayList()

        val rating = details.rating
        if (rating != null) {
            titleDetails.add(
                Pair(
                    context.getString(R.string.rating),
                    context.getString(getRatingLabel(rating))
                )
            )
        }

        val source = details.source
        if (source != null) {
            titleDetails.add(
                Pair(
                    context.getString(R.string.source),
                    source.replace("_", " ").capitalize()
                )
            )
        }

        val studios = details.animeStudios
        if (!studios.isNullOrEmpty()) {
            val labels = studios.map { it.name }
            titleDetails.add(
                Pair(
                    context.getString(R.string.studios),
                    TextUtils.join(", ", labels)
                )
            )
        }

        val authors = details.mangaAuthors
        if (!authors.isNullOrEmpty()) {
            val labels = authors.map { "${it.name} (${it.role})" }
            titleDetails.add(
                Pair(
                    context.getString(R.string.authors),
                    TextUtils.join(", ", labels)
                )
            )
        }

        val serialization = details.serialization
        if (serialization != null) {
            val labels = serialization.map { it.name }
            titleDetails.add(
                Pair(
                    context.getString(R.string.serialization),
                    TextUtils.join(", ", labels)
                )
            )
        }

        val popularity = details.popularity
        if (popularity != null) {
            titleDetails.add(
                Pair(
                    context.getString(R.string.details_info__popularity),
                    "#${popularity.formatToSeparatedText()}"
                )
            )
        }

        titleDetails.add(
            Pair(
                context.getString(R.string.details_info__members),
                details.members.formatToSeparatedText()
            )
        )

        return titleDetails
    }

    private fun getRelatedTitles(details: TitleDetails): List<RelatedTitle> {

        val finalList = mutableListOf<RelatedTitle>()
        /*if (details.relatedAnime != null) {
            finalList.addAll(getRelatedSections(details.relatedAnime, TitleType.ANIME))
        }
        if (details.relatedManga != null) {
            finalList.addAll(getRelatedSections(details.relatedManga, TitleType.MANGA))
        }*/
        return finalList
    }

    /*private fun getRelatedSections(
        titles: List<RelatedTitle>,
        titleType: TitleType
    ): List<RelatedTitle> {

        val relationTypes = titles
            .distinctBy { it.relationType }
            .map { it.relationType }

        val relatedSections = relationTypes
            .map { type ->
                titles.filter { type == it.relationType }
            }

        val finalList = mutableListOf<RelatedTitle>()
        relatedSections.forEach { list ->
            finalList.add(
                RelatedTitle(0, list[0].relationType, LABEL, titleType)
            )
            finalList.addAll(
                list.map {
                    RelatedTitle(it.id, it.name, LINK, titleType)
                }
            )
        }
        return finalList
    }*/
}