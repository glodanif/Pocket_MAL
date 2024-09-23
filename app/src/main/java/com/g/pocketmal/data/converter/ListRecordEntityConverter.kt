package com.g.pocketmal.data.converter

import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.database.model.TitleDetailsTable
import com.g.pocketmal.domain.entity.ListRecordEntity
import com.g.pocketmal.data.common.Status

class ListRecordEntityConverter {

    fun transform(record: DbListRecord): ListRecordEntity {

        return ListRecordEntity(
            record.seriesId,
            record.seriesTitle,
            record.seriesType,
            record.seriesEpisodes,
            record.seriesSubEpisodes,
            record.seriesStatus,
            record.seriesImage,
            record.myStartDate,
            record.myFinishDate,
            record.myEpisodes,
            record.mySubEpisodes,
            record.myScore,
            record.myStatus,
            record.myRe,
            record.myReValue,
            record.myReTimes,
            record.myLastUpdated,
            record.myTags,
            record.myComments,
            record.myPriority,
            record.titleType
        )
    }

    fun transform(record: TitleDetailsTable): DbListRecord {

        val english = record.englishTitle
        val englishTitleLabel = if (!english.isNullOrEmpty()) english else record.title

        return DbListRecord(
            0,
            record.id,
            record.title,
            englishTitleLabel,
            record.type,
            record.episodes,
            record.subEpisodes,
            record.status,
            record.imageUrl,
            //FIXME
            null,
            null,
            null,
            0,
            0,
            0,
            Status.PLANNED,
            false,
            0,
            0,
            System.currentTimeMillis(),
            arrayListOf(),
            "",
            0,
            record.titleType
        )
    }
}
