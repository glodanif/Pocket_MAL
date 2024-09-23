package com.g.pocketmal.data.database.converter

import com.g.pocketmal.data.api.response.TitleDetailsResponse
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.database.model.TitleDetailsTable
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.data.common.Status
import java.text.SimpleDateFormat

class TitleDetailsDataConverter {

    private val updateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

    fun transform(details: TitleDetailsResponse, titleType: TitleType): TitleDetailsTable {

        return TitleDetailsTable(
            0,
            details.id,
            titleType,
            details.startDate,
            details.endDate,
            details.mediaType,
            details.status,
            if (titleType == TitleType.ANIME) details.numEpisodes else details.numChapters,
            if (titleType == TitleType.ANIME) 0 else details.numVolumes,
            details.mainPicture?.large,
            details.synopsis,
            details.title,
            details.alternativeTitles?.english,
            details.alternativeTitles?.synonyms,
            details.alternativeTitles?.japanese,
            details.meanScore,
            details.numScoringUsers,
            details.rank,
            details.popularity,
            details.numListUsers,
            "",
            details.rating,
            details.averageEpisodeDuration,
            details.serialization,
            details.source,
            details.broadcast,
            details.startSeason,
            details.authors,
            details.studios,
            details.genres,
            details.relatedAnime,
            details.relatedManga,
            arrayListOf(),
            arrayListOf()
        )
    }

    fun transformToRecord(details: TitleDetailsResponse, titleType: TitleType): DbListRecord {

        val isAnime = titleType == TitleType.ANIME
        val listStatus = details.myListStatus

        val english = details.alternativeTitles?.english
        val englishTitleLabel = if (!english.isNullOrEmpty()) english else details.title

        val seriesEpisodes = (if (isAnime) details.numEpisodes else details.numChapters)
        val seriesSubEpisodes = (if (isAnime) 0 else details.numVolumes)

        val myEpisodes =
            (if (isAnime) listStatus?.numEpisodesWatched else listStatus?.numChaptersRead)
                ?: 0
        val mySubEpisodes = (if (isAnime) 0 else listStatus?.numVolumesRead) ?: 0

        val score = listStatus?.score ?: 0

        var statusLabel = listStatus?.status
        if (statusLabel != null) {
            statusLabel = statusLabel.replace("reading", "watching")
            statusLabel = statusLabel.replace("plan_to_read", "plan_to_watch")
        }

        val status = Status.from(statusLabel)
        val re = (if (isAnime) listStatus?.isRewatching else listStatus?.isRereading) ?: false
        val reValue = (if (isAnime) listStatus?.rewatchValue else listStatus?.rereadValue) ?: 0
        val reTimes = (if (isAnime) listStatus?.numTimesRewatch else listStatus?.numTimesReread)
            ?: 0

        val tags = listStatus?.tags ?: arrayListOf()

        var lastUpdated = 0L
        val updatedAt = listStatus?.updatedAt
        if (updatedAt != null) {
            lastUpdated = updateFormatter.parse(updatedAt)?.time ?: 0
        }

        return DbListRecord(
            0,
            details.id,
            details.title,
            englishTitleLabel,
            details.mediaType,
            seriesEpisodes,
            seriesSubEpisodes,
            details.status,
            details.mainPicture?.large,
            details.nsfw,
            listStatus?.startDate,
            listStatus?.finishDate,
            myEpisodes,
            mySubEpisodes,
            score,
            status,
            re,
            reValue,
            reTimes,
            lastUpdated,
            tags,
            listStatus?.comments,
            listStatus?.priority ?: 0,
            titleType
        )
    }
}