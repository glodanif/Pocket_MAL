package com.g.pocketmal.data.keyvalue

import android.content.Context
import com.g.pocketmal.R

data class SharingPatterns(
    val animeRated: String,
    val animeRewatched: String,
    val animeStarted: String,
    val animeStartedFromEpisode: String,
    val animeEpisodes: String,
    val animeEpisodesWithEpisodes: String,
    val animeCompleted: String,
    val animeCompletedWithScore: String,
    val animeOnHold: String,
    val animeDropped: String,
    val animePlanned: String,
    val mangaRated: String,
    val mangaReread: String,
    val mangaChapters: String,
    val mangaChaptersWithChapters: String,
    val mangaVolumes: String,
    val mangaVolumesWithVolumes: String,
    val mangaStarted: String,
    val mangaStartedFromChapter: String,
    val mangaStartedFromVolume: String,
    val mangaCompleted: String,
    val mangaCompletedWithScore: String,
    val mangaOnHold: String,
    val mangaOnHoldVolumes: String,
    val mangaDropped: String,
    val mangaDroppedVolumes: String,
    val mangaPlanned: String,
) {

    companion object {
        fun getDefault(context: Context): SharingPatterns {
            return SharingPatterns(
                animeRated = context.getString(R.string.ratedAnime),
                animeRewatched = context.getString(R.string.rewatchedAnime),
                animeStarted = context.getString(R.string.startedToWatchAnime),
                animeStartedFromEpisode = context.getString(R.string.resumedToWatchAnime),
                animeEpisodes = context.getString(R.string.watchingAnime),
                animeEpisodesWithEpisodes = context.getString(R.string.watchingWithSeriesEpisodesAnime),
                animeCompleted = context.getString(R.string.completedAnime),
                animeCompletedWithScore = context.getString(R.string.completedWithScoreAnime),
                animeOnHold = context.getString(R.string.onHoldAnime),
                animeDropped = context.getString(R.string.droppedAnime),
                animePlanned = context.getString(R.string.planToWatchAnime),
                mangaRated = context.getString(R.string.ratedManga),
                mangaReread = context.getString(R.string.rereadManga),
                mangaChapters = context.getString(R.string.readingChapterManga),
                mangaChaptersWithChapters = context.getString(R.string.readingChapterWithSeriesChapterManga),
                mangaVolumes = context.getString(R.string.readingVolumeManga),
                mangaVolumesWithVolumes = context.getString(R.string.readingVolumesWithSeriesVolumesManga),
                mangaStarted = context.getString(R.string.startedToReadManga),
                mangaStartedFromChapter = context.getString(R.string.resumedToReadChaptersManga),
                mangaStartedFromVolume = context.getString(R.string.resumedToReadVolumesManga),
                mangaCompleted = context.getString(R.string.completedManga),
                mangaCompletedWithScore = context.getString(R.string.completedWithScoreManga),
                mangaOnHold = context.getString(R.string.onHoldManga),
                mangaOnHoldVolumes = context.getString(R.string.onHoldVolumesManga),
                mangaDropped = context.getString(R.string.droppedManga),
                mangaDroppedVolumes = context.getString(R.string.droppedVolumesManga),
                mangaPlanned = context.getString(R.string.planToReadManga)
            )
        }
    }
}
