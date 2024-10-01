package com.g.pocketmal.ui.legacy.viewentity.converter

import android.content.Context
import android.text.TextUtils
import com.g.pocketmal.R
import com.g.pocketmal.domain.MAL_HOST
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.entity.ListRecord
import com.g.pocketmal.reformatToViewableDate
import com.g.pocketmal.ui.legacy.viewentity.RecordViewModel
import com.g.pocketmal.util.list.DataInterpreter

class ListItemConverter(private val context: Context) {

    fun transform(record: ListRecord): RecordViewModel {

        val isAnime = record.titleType == TitleType.ANIME

        val seriesStatusTextId = when (record.seriesStatus) {
            "finished" -> R.string.finished
            "currently_publishing" -> R.string.publishing
            "not_yet_published" -> R.string.notYetPublished
            "currently_airing" -> R.string.airing
            "finished_airing" -> R.string.finished
            "not_yet_aired" -> R.string.notYetAired
            else -> R.string.unknown
        }

        val seriesTypeTextId = when (record.seriesType) {
            "tv" -> R.string.tv
            "ova" -> R.string.ova
            "movie" -> R.string.movie
            "special" -> R.string.special
            "ona" -> R.string.ona
            "music" -> R.string.musical
            "manga" -> R.string.manga
            "novel" -> R.string.novel
            "one_shot" -> R.string.oneShot
            "doujinshi" -> R.string.doujinshi
            "manhwa" -> R.string.manhwa
            "manhua" -> R.string.manhua
            "oel" -> R.string.oel
            else -> R.string.unknown
        }

        val discussionLink = String.format("%s/forum/?%s=%s", MAL_HOST,
                if (isAnime) "animeid" else "mangaid", record.seriesId)

        val malLink = String.format("%s%s/%s", MAL_HOST,
                if (isAnime) "anime" else "manga", record.seriesId)

        val myScoreLabel = context.getString(R.string.scoreList,
                if (record.myScore == 0) "—" else record.myScore)

        val seriesEpisodes = record.seriesEpisodes
        val seriesSubEpisodes = record.seriesSubEpisodes
        val seriesEpisodesLabel = if (seriesEpisodes == 0) "—" else seriesEpisodes.toString()
        val seriesSubEpisodesLabel = if (seriesSubEpisodes == 0) "—" else seriesSubEpisodes.toString()

        val shortIncrementSubEpisodesLabel = if (isAnime)
            "" else context.getString(R.string.plusOneVolume)
        val shortIncrementEpisodesLabel = context.getString(if (isAnime)
            R.string.plusOne else R.string.plusOneChapter)
        val reLabel = context.getString(if (isAnime)
            R.string.rewatching else R.string.rereading)

        var fullEpisodesLabel = String.format("%d / %s", record.myEpisodes, seriesEpisodesLabel)
        if (!isAnime) {
            fullEpisodesLabel += String.format("   %d / %s", record.mySubEpisodes, seriesSubEpisodesLabel)
        }

        val episodesTypeLabel = context.getString(
                if (isAnime) R.string.episodes else R.string.chapters)
        val subEpisodesTypeLabel = if (isAnime) "" else context.getString(R.string.volumes)

        val statusLabel = context.getString(
                DataInterpreter.getStatusById(record.myStatus, record.titleType))

        val startDateLabel = record.myStartDate
                .reformatToViewableDate(context.getString(R.string.unknown))
        val finishDateLabel = record.myFinishDate
                .reformatToViewableDate(context.getString(R.string.unknown))

        return RecordViewModel(
            seriesId = record.seriesId,
            seriesTitle = record.seriesTitle,
            seriesMediaType = record.seriesType,
            seriesEpisodes = record.seriesEpisodes,
            seriesSubEpisodes = record.seriesSubEpisodes,
            seriesStatus = record.seriesStatus,
            seriesImage = record.seriesImage,
            myEpisodes = record.myEpisodes,
            mySubEpisodes = record.mySubEpisodes,
            myScore = record.myScore,
            myStatus = record.myStatus,
            myStatusLabel = statusLabel,
            myRe = record.myRe,
            myReTimes = record.myReTimes,
            myTags = TextUtils.join(", ", record.myTags),
            myStartDate = startDateLabel,
            myFinishDate = finishDateLabel,
            myComments = record.myComments ?: "",
            updatedAt = record.myLastUpdated,
            recordType = record.titleType,
            seriesStatusText = context.getString(seriesStatusTextId),
            seriesTypeText = context.getString(seriesTypeTextId),
            discussionLink = discussionLink,
            malLink = malLink,
            myScoreLabel = myScoreLabel,
            seriesEpisodesLabel = seriesEpisodesLabel,
            seriesSubEpisodesLabel = seriesSubEpisodesLabel,
            shortIncrementEpisodesLabel = shortIncrementEpisodesLabel,
            shortIncrementSubEpisodesLabel = shortIncrementSubEpisodesLabel,
            reLabel = reLabel,
            fullEpisodesLabel = fullEpisodesLabel,
            episodesTypeLabel = episodesTypeLabel,
            subEpisodesTypeLabel = subEpisodesTypeLabel,
            withSubEpisodes = record.titleType == TitleType.MANGA
        )
    }

    fun transform(records: List<ListRecord>): List<RecordViewModel> {
        return records.map { transform(it) }
    }

    fun createFrame(id: Int, type: TitleType): RecordViewModel {
        return createFrame(id, type, null)
    }

    fun createFrame(id: Int, type: TitleType, detailsViewModel: com.g.pocketmal.ui.legacy.viewentity.TitleDetailsViewModel?): RecordViewModel {

        val discussionLink = String.format("%s/forum/?%s=%s", MAL_HOST,
                if (type == TitleType.ANIME) "animeid" else "mangaid", id)

        val malLink = String.format("%s%s/%s", MAL_HOST,
                if (type == TitleType.ANIME) "anime" else "manga", id)

        return RecordViewModel(
            seriesId = id,
            recordType = type,
            seriesImage = detailsViewModel?.imageUrl ?: "<empty>",
            seriesTitle = detailsViewModel?.title ?: "",
            seriesStatus = "",
            seriesMediaType = "",
            seriesEpisodes = 0,
            myStatusLabel = "",
            myTags = "",
            myStartDate = context.getString(R.string.unknown),
            myFinishDate = context.getString(R.string.unknown),
            myComments = "",
            seriesStatusText = "",
            seriesTypeText = "",
            discussionLink = discussionLink,
            malLink = malLink,
            myScoreLabel = "",
            seriesEpisodesLabel = "",
            seriesSubEpisodesLabel = "",
            shortIncrementEpisodesLabel = "",
            shortIncrementSubEpisodesLabel = "",
            reLabel = "",
            fullEpisodesLabel = "",
            episodesTypeLabel = "",
            subEpisodesTypeLabel = "",
            withSubEpisodes = type == TitleType.MANGA
        )
    }
}
