package com.g.pocketmal.ui.legacy.viewentity.converter

import android.content.Context
import android.text.TextUtils
import com.g.pocketmal.R
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel
import com.g.pocketmal.util.EpisodeType
import com.g.pocketmal.util.list.DataInterpreter
import java.util.*

class ListRecordConverter(private val context: Context) {

    fun transform(titleType: TitleType, record: DbListRecord, useEnglishTitle: Boolean): RecordListViewModel {

        val isAnime = titleType == TitleType.ANIME

        return RecordListViewModel(
            seriesId = record.seriesId,
            seriesTitle = if (useEnglishTitle) record.seriesEnglishTitle else record.seriesTitle,
            seriesMediaType = DataInterpreter.getMediaTypeLabelFromNetworkConst(record.seriesType),
            seriesMediaTypeRaw = record.seriesType,
            seriesPosterUrl = record.seriesImage,
            seriesEpisodes = record.seriesEpisodes,
            seriesSubEpisodes = record.seriesSubEpisodes,
            seriesStatus = DataInterpreter.getSeriesStatusFromNetworkConst(record.seriesStatus),
            isSubEpisodesAvailable = !isAnime,
            myEpisodes = record.myEpisodes,
            mySubEpisodes = record.mySubEpisodes,
            myScore = record.myScore,
            myScoreLabel = if (record.myScore == 0) "—" else record.myScore.toString(),
            myTags = if (record.myTags.isNotEmpty()) TextUtils.join(", ", record.myTags) else null,
            episodesName = context.getString(if (isAnime) R.string.episodes else R.string.chapters),
            subEpisodesName = if (isAnime) "" else context.getString(R.string.volumes),
            allEpisodesFinishedText = if (isAnime) R.string.watchedAllEpisodes else R.string.readLastChapter,
            isRe = record.myRe,
            reLabel = context.getString(if (isAnime) R.string.rewatching else R.string.rereading),
            lastUpdated = record.myLastUpdated,
            episodesLabel = String.format(
                "%s / %s",
                record.myEpisodes,
                if (record.seriesEpisodes != 0) record.seriesEpisodes.toString() else "—"
            ),
            subEpisodesLabel = String.format(
                "%s / %s",
                record.mySubEpisodes,
                if (record.seriesSubEpisodes != 0) record.seriesSubEpisodes.toString() else "—"
            ),
            episodesType = if (isAnime) EpisodeType.EPISODE else EpisodeType.CHAPTER,
            subEpisodesType = if (isAnime) EpisodeType.EPISODE else EpisodeType.VOLUME
        )
    }

    fun transform(titleType: TitleType, records: List<DbListRecord>, useEnglishTitle: Boolean): List<RecordListViewModel> {

        val viewModels = ArrayList<RecordListViewModel>()
        for (record in records) {
            viewModels.add(transform(titleType, record, useEnglishTitle))
        }
        return viewModels
    }
}
