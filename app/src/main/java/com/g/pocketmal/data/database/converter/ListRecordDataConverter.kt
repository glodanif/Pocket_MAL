package com.g.pocketmal.data.database.converter

import android.util.Log
import com.g.pocketmal.data.api.response.ListItem
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.common.Status
import java.text.SimpleDateFormat

class ListRecordDataConverter {

    private val updateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz")

    fun transform(record: ListItem, titleType: TitleType): DbListRecord {

        val isAnime = titleType == TitleType.ANIME
        val node = record.node
        val listStatus = node.listStatus

        val english = node.alternativeTitles?.english
        val englishTitleLabel = if (!english.isNullOrEmpty()) english else node.title

        val seriesEpisodes = (if (isAnime) node.numEpisodes else node.numChapters) ?: 0
        val seriesSubEpisodes = (if (isAnime) 0 else node.numVolumes) ?: 0

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
            node.id,
            node.title,
            englishTitleLabel,
            node.mediaType,
            seriesEpisodes,
            seriesSubEpisodes,
            node.status,
            node.mainPicture?.large,
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