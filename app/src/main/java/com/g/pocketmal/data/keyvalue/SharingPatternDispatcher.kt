package com.g.pocketmal.data.keyvalue

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

import androidx.annotation.StringRes

import com.g.pocketmal.R
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.ordinal
import com.g.pocketmal.util.Action

import java.util.ArrayList

class SharingPatternDispatcher(private val context: Context) {
    private val preferences: SharedPreferences

    private val animeRatedPattern: String
        get() = preferences.getString(KEY_ANIME_RATED, context.getString(R.string.ratedAnime)) ?: ""

    private val animeTagsUpdatedPattern: String
        get() = preferences
                .getString(KEY_ANIME_TAGS_UPDATED, context.getString(R.string.changedTagsAnime))
                ?: ""

    private val animeRewatchedPattern: String
        get() = preferences.getString(KEY_ANIME_REWATCHED, context.getString(R.string.rewatchedAnime))
                ?: ""

    private val animeEpisodesPattern: String
        get() = preferences
                .getString(KEY_ANIME_EPISODES, context.getString(R.string.watchingAnime)) ?: ""

    private val animeEpisodesWithEpisodesPattern: String
        get() = preferences
                .getString(KEY_ANIME_EPISODES_WITH_EPISODES, context.getString(R.string.watchingWithSeriesEpisodesAnime))
                ?: ""

    private val animeStartedWatchingPattern: String
        get() = preferences
                .getString(KEY_ANIME_STARTED, context.getString(R.string.startedToWatchAnime)) ?: ""

    private val animeStartedWatchingFromEpisodePattern: String
        get() = preferences
                .getString(KEY_ANIME_STARTED_FROM_EPISODE, context.getString(R.string.resumedToWatchAnime))
                ?: ""

    private val animeCompletedPattern: String
        get() = preferences
                .getString(KEY_ANIME_COMPLETED, context.getString(R.string.completedAnime)) ?: ""

    private val animeCompletedWithScorePattern: String
        get() = preferences
                .getString(KEY_ANIME_COMPLETED_WITH_SCORE, context.getString(R.string.completedWithScoreAnime))
                ?: ""

    private val animeOnHoldPattern: String
        get() = preferences
                .getString(KEY_ANIME_ON_HOLD, context.getString(R.string.onHoldAnime)) ?: ""

    private val animeDroppedPattern: String
        get() = preferences
                .getString(KEY_ANIME_DROPPED, context.getString(R.string.droppedAnime)) ?: ""

    private val animePlanToWatchPattern: String
        get() = preferences
                .getString(KEY_ANIME_PLANNED, context.getString(R.string.planToWatchAnime)) ?: ""

    private val mangaRatedPattern: String
        get() = preferences
                .getString(KEY_MANGA_RATED, context.getString(R.string.ratedManga)) ?: ""

    private val mangaTagsUpdatedPattern: String
        get() = preferences
                .getString(KEY_MANGA_TAGS_UPDATED, context.getString(R.string.changedTagsManga))
                ?: ""

    private val mangaRereadPattern: String
        get() = preferences
                .getString(KEY_MANGA_REREAD, context.getString(R.string.rereadManga)) ?: ""

    private val mangaChaptersPattern: String
        get() = preferences
                .getString(KEY_MANGA_CHAPTERS, context.getString(R.string.readingChapterManga))
                ?: ""

    private val mangaChaptersWithChaptersPattern: String
        get() = preferences
                .getString(KEY_MANGA_CHAPTERS_WITH_CHAPTERS, context.getString(R.string.readingChapterWithSeriesChapterManga))
                ?: ""

    private val mangaVolumesPattern: String
        get() = preferences
                .getString(KEY_MANGA_VOLUMES, context.getString(R.string.readingVolumeManga)) ?: ""

    private val mangaVolumesWithVolumesPattern: String
        get() = preferences
                .getString(KEY_MANGA_VOLUMES_WITH_VOLUMES, context.getString(R.string.readingVolumesWithSeriesVolumesManga))
                ?: ""

    private val mangaStartedReadingPattern: String
        get() = preferences
                .getString(KEY_MANGA_STARTED, context.getString(R.string.startedToReadManga)) ?: ""

    private val mangaStartedReadingFromChapterPattern: String
        get() = preferences
                .getString(KEY_MANGA_STARTED_FROM_CHAPTER, context.getString(R.string.resumedToReadChaptersManga))
                ?: ""

    private val mangaStartedReadingFromVolumePattern: String
        get() = preferences
                .getString(KEY_MANGA_STARTED_FROM_VOLUME, context.getString(R.string.resumedToReadVolumesManga))
                ?: ""

    private val mangaCompletedPattern: String
        get() = preferences
                .getString(KEY_MANGA_COMPLETED, context.getString(R.string.completedManga)) ?: ""

    private val mangaCompletedWithScorePattern: String
        get() = preferences
                .getString(KEY_MANGA_COMPLETED_WITH_SCORE, context.getString(R.string.completedWithScoreManga))
                ?: ""

    private val mangaOnHoldPattern: String
        get() = preferences
                .getString(KEY_MANGA_ON_HOLD, context.getString(R.string.onHoldManga)) ?: ""

    private val mangaOnHoldVolumesPattern: String
        get() = preferences
                .getString(KEY_MANGA_ON_HOLD_VOLUMES, context.getString(R.string.onHoldVolumesManga))
                ?: ""

    private val mangaDroppedPattern: String
        get() = preferences
                .getString(KEY_MANGA_DROPPED, context.getString(R.string.droppedManga)) ?: ""

    private val mangaDroppedVolumesPattern: String
        get() = preferences
                .getString(KEY_MANGA_DROPPED_VOLUMES, context.getString(R.string.droppedVolumesManga))
                ?: ""

    private val mangaPlanToReadPattern: String
        get() = preferences
                .getString(KEY_MANGA_PLANNED, context.getString(R.string.planToReadManga)) ?: ""

    init {
        this.preferences = context.getSharedPreferences(KEY_ROOT, Context.MODE_PRIVATE)
    }

    fun getSharingPatterns(type: TitleType): List<SharingPattern> {
        return getPattersList(type)
    }

    private fun getPattersList(type: TitleType): List<SharingPattern> {

        val patterns = ArrayList<SharingPattern>()

        if (type === TitleType.ANIME) {
            patterns.add(cretePattern(KEY_ANIME_RATED, R.string.sharing_pattern__name_rated, R.string.ratedAnime))
            //patterns.add(cretePattern(KEY_ANIME_TAGS_UPDATED, R.string.sharing_pattern__name_tags, R.string.changedTagsAnime))
            patterns.add(cretePattern(KEY_ANIME_REWATCHED, R.string.sharing_pattern__name_rewatched, R.string.rewatchedAnime))
            patterns.add(cretePattern(KEY_ANIME_STARTED, R.string.sharing_pattern__name_started, R.string.startedToWatchAnime))
            patterns.add(cretePattern(KEY_ANIME_STARTED_FROM_EPISODE, R.string.sharing_pattern__name_started_from_episode, R.string.resumedToWatchAnime))
            patterns.add(cretePattern(KEY_ANIME_EPISODES, R.string.sharing_pattern__name_watched, R.string.watchingAnime))
            patterns.add(cretePattern(KEY_ANIME_EPISODES_WITH_EPISODES, R.string.sharing_pattern__name_watched_with_episodes, R.string.watchingWithSeriesEpisodesAnime))
            patterns.add(cretePattern(KEY_ANIME_COMPLETED, R.string.sharing_pattern__name_completed, R.string.completedAnime))
            patterns.add(cretePattern(KEY_ANIME_COMPLETED_WITH_SCORE, R.string.sharing_pattern__name_completed_and_scored, R.string.completedWithScoreAnime))
            patterns.add(cretePattern(KEY_ANIME_ON_HOLD, R.string.sharing_pattern__name_on_hold, R.string.onHoldAnime))
            patterns.add(cretePattern(KEY_ANIME_DROPPED, R.string.sharing_pattern__name_dropped, R.string.droppedAnime))
            patterns.add(cretePattern(KEY_ANIME_PLANNED, R.string.sharing_pattern__name_planned, R.string.planToWatchAnime))
        } else {
            patterns.add(cretePattern(KEY_MANGA_RATED, R.string.sharing_pattern__name_rated, R.string.ratedManga))
            //patterns.add(cretePattern(KEY_MANGA_TAGS_UPDATED, R.string.sharing_pattern__name_tags, R.string.changedTagsManga))
            patterns.add(cretePattern(KEY_MANGA_REREAD, R.string.sharing_pattern__name_reread, R.string.rereadManga))
            patterns.add(cretePattern(KEY_MANGA_STARTED, R.string.sharing_pattern__name_started, R.string.startedToReadManga))
            patterns.add(cretePattern(KEY_MANGA_STARTED_FROM_CHAPTER, R.string.sharing_pattern__name_started_from_chapter, R.string.resumedToReadChaptersManga))
            patterns.add(cretePattern(KEY_MANGA_STARTED_FROM_VOLUME, R.string.sharing_pattern__name_started_from_volume, R.string.resumedToReadVolumesManga))
            patterns.add(cretePattern(KEY_MANGA_CHAPTERS, R.string.sharing_pattern__name_read_chapters, R.string.readingChapterManga))
            patterns.add(cretePattern(KEY_MANGA_CHAPTERS_WITH_CHAPTERS, R.string.sharing_pattern__name_read_chapters_with_chapters, R.string.readingChapterWithSeriesChapterManga))
            patterns.add(cretePattern(KEY_MANGA_VOLUMES, R.string.sharing_pattern__name_read_volumes, R.string.readingVolumeManga))
            patterns.add(cretePattern(KEY_MANGA_VOLUMES_WITH_VOLUMES, R.string.sharing_pattern__name_read_volumes_with_volumes, R.string.readingVolumesWithSeriesVolumesManga))
            patterns.add(cretePattern(KEY_MANGA_COMPLETED, R.string.sharing_pattern__name_completed, R.string.completedManga))
            patterns.add(cretePattern(KEY_MANGA_COMPLETED_WITH_SCORE, R.string.sharing_pattern__name_completed_and_scored, R.string.completedWithScoreManga))
            patterns.add(cretePattern(KEY_MANGA_ON_HOLD, R.string.sharing_pattern__name_on_hold, R.string.onHoldManga))
            patterns.add(cretePattern(KEY_MANGA_ON_HOLD_VOLUMES, R.string.sharing_pattern__name_on_hold_with_volumes, R.string.onHoldVolumesManga))
            patterns.add(cretePattern(KEY_MANGA_DROPPED, R.string.sharing_pattern__name_dropped, R.string.droppedManga))
            patterns.add(cretePattern(KEY_MANGA_DROPPED_VOLUMES, R.string.sharing_pattern__name_dropped_with_volumes, R.string.droppedVolumesManga))
            patterns.add(cretePattern(KEY_MANGA_PLANNED, R.string.sharing_pattern__name_planned, R.string.planToReadManga))
        }

        return patterns
    }

    private fun cretePattern(key: String, @StringRes name: Int, @StringRes defaultValue: Int): SharingPattern {
        return SharingPattern(key, context.getString(name), context.getString(defaultValue), getPattern(key, defaultValue))
    }

    private fun getPattern(key: String, @StringRes defaultValue: Int): String {
        return preferences
                .getString(key, context.getString(defaultValue)) ?: ""
    }

    fun storePattern(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getPattern(title: com.g.pocketmal.ui.legacy.viewmodel.RecordViewModel, action: Action): String {
        return try {
            getFinalText(title, if (title.recordType === TitleType.ANIME)
                getAnimePattern(title, action)
            else
                getMangaPattern(title, action))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    @SuppressLint("SwitchIntDef")
    private fun getAnimePattern(title: com.g.pocketmal.ui.legacy.viewmodel.RecordViewModel, action: Action): String {

        when (action) {
            Action.ACTION_SCORE -> return animeRatedPattern
            //Action.ACTION_TAGS -> return animeTagsUpdatedPattern
            Action.ACTION_REWATCHED -> return animeRewatchedPattern
            Action.ACTION_STATUS -> return getStatusChangedPattern(title)
            Action.ACTION_EPISODES -> return if (title.seriesEpisodes > 0)
                animeEpisodesWithEpisodesPattern
            else
                animeEpisodesPattern
            else -> return context.getString(R.string.shareTitleText, title.seriesTitle, title.malLink)
        }
    }

    @SuppressLint("SwitchIntDef")
    private fun getMangaPattern(title: com.g.pocketmal.ui.legacy.viewmodel.RecordViewModel, action: Action): String {

        return when (action) {
            Action.ACTION_SCORE -> mangaRatedPattern
            //Action.ACTION_TAGS -> return mangaTagsUpdatedPattern
            Action.ACTION_REWATCHED -> mangaRereadPattern
            Action.ACTION_STATUS -> getStatusChangedPattern(title)
            Action.ACTION_CHAPTERS -> if (title.seriesEpisodes > 0)
                mangaChaptersWithChaptersPattern
            else
                mangaChaptersPattern
            Action.ACTION_VOLUMES -> if (title.seriesSubEpisodes > 0)
                mangaVolumesWithVolumesPattern
            else
                mangaVolumesPattern
            else -> context.getString(R.string.shareTitleText, title.seriesTitle, title.malLink)
        }
    }

    private fun getStatusChangedPattern(title: com.g.pocketmal.ui.legacy.viewmodel.RecordViewModel): String {

        val isAnime = title.recordType === TitleType.ANIME
        val isScored = title.myScore > 0
        val isVolumes = !isAnime && title.myEpisodes == 0
        val episodes = title.myEpisodes
        val subEpisodes = if (title.withSubEpisodes)
            0
        else
            title.mySubEpisodes

        when (title.myStatus) {

            Status.IN_PROGRESS -> {

                return if (isAnime) {
                    if (episodes > 1)
                        animeStartedWatchingFromEpisodePattern
                    else
                        animeStartedWatchingPattern
                } else {
                    when {
                        episodes > 1 -> {
                            mangaStartedReadingFromChapterPattern
                        }
                        subEpisodes > 1 -> {
                            mangaStartedReadingFromVolumePattern
                        }
                        else -> {
                            mangaStartedReadingPattern
                        }
                    }
                }
            }

            Status.COMPLETED -> return if (isAnime)
                if (isScored) animeCompletedWithScorePattern else animeCompletedPattern
            else if (isScored) mangaCompletedWithScorePattern else mangaCompletedPattern
            Status.ON_HOLD -> return if (isAnime)
                animeOnHoldPattern
            else if (!isVolumes) mangaOnHoldPattern else mangaOnHoldVolumesPattern
            Status.DROPPED -> return if (isAnime)
                animeDroppedPattern
            else if (!isVolumes) mangaDroppedPattern else mangaDroppedVolumesPattern
            Status.PLANNED -> return if (isAnime) animePlanToWatchPattern else mangaPlanToReadPattern
            else -> return context.getString(R.string.shareTitleText, title.seriesTitle, title.malLink)
        }
    }

    private fun getFinalText(title: com.g.pocketmal.ui.legacy.viewmodel.RecordViewModel, pattern: String): String {

        val myEpisodes = title.myEpisodes
        val mySubEpisodes = title.mySubEpisodes
        val seriesEpisodes = title.seriesEpisodes
        val seriesSubEpisodes = title.seriesSubEpisodes

        return pattern
                .replace("{title}", title.seriesTitle)
                //.replace("{tags}", title.getMyTags())
                .replace("{score}", title.myScore.toString())
                .replace("{watched_episodes}", myEpisodes.toString())
                .replace("{watched_episodes?}", myEpisodes.ordinal())
                .replace("{read_chapters}", myEpisodes.toString())
                .replace("{read_chapters?}", myEpisodes.ordinal())
                .replace("{read_volumes}", mySubEpisodes.toString())
                .replace("{read_volumes?}", mySubEpisodes.ordinal())
                .replace("{series_episodes}", seriesEpisodes.toString())
                .replace("{series_chapters}", seriesEpisodes.toString())
                .replace("{series_volumes}", seriesSubEpisodes.toString())
                .replace("{link}", title.malLink) + context.getString(R.string.sharing_pattern__from)
    }

    fun reset() {
        preferences.edit().clear().apply()
    }

    class SharingPattern internal constructor(val key: String, val name: String, val defaultValue: String, val currentValue: String)

    companion object {

        private val KEY_ROOT = "key.patterns"

        private val KEY_ANIME_RATED = "key.anime_rated"
        private val KEY_ANIME_TAGS_UPDATED = "key.anime_tags_updated"
        private val KEY_ANIME_REWATCHED = "key.anime_rewatched"
        private val KEY_ANIME_STARTED = "key.anime_started"
        private val KEY_ANIME_STARTED_FROM_EPISODE = "key.anime_started_from_episode"
        private val KEY_ANIME_EPISODES = "key.anime_episodes"
        private val KEY_ANIME_EPISODES_WITH_EPISODES = "key.anime_episodes_with_episodes"
        private val KEY_ANIME_COMPLETED = "key.anime_completed"
        private val KEY_ANIME_COMPLETED_WITH_SCORE = "key.anime_completed_with_score"
        private val KEY_ANIME_ON_HOLD = "key.anime_on_hold"
        private val KEY_ANIME_DROPPED = "key.anime_dropped"
        private val KEY_ANIME_PLANNED = "key.anime_planned"

        private val KEY_MANGA_RATED = "key.manga_rated"
        private val KEY_MANGA_TAGS_UPDATED = "key.manga_tags_updated"
        private val KEY_MANGA_REREAD = "key.manga_reread"
        private val KEY_MANGA_CHAPTERS = "key.manga_chapters"
        private val KEY_MANGA_CHAPTERS_WITH_CHAPTERS = "key.manga_chapters_with_chapters"
        private val KEY_MANGA_VOLUMES = "key.manga_volumes"
        private val KEY_MANGA_VOLUMES_WITH_VOLUMES = "key.manga_volumes_with_volumes"
        private val KEY_MANGA_STARTED = "key.manga_started"
        private val KEY_MANGA_STARTED_FROM_CHAPTER = "key.manga_started_from_chapter"
        private val KEY_MANGA_STARTED_FROM_VOLUME = "key.manga_started_from_volume"
        private val KEY_MANGA_COMPLETED = "key.manga_completed"
        private val KEY_MANGA_COMPLETED_WITH_SCORE = "key.manga_completed_with_score"
        private val KEY_MANGA_ON_HOLD = "key.manga_on_hold"
        private val KEY_MANGA_ON_HOLD_VOLUMES = "key.manga_on_hold_volumes"
        private val KEY_MANGA_DROPPED = "key.manga_dropped"
        private val KEY_MANGA_DROPPED_VOLUMES = "key.manga_dropped_volumes"
        private val KEY_MANGA_PLANNED = "key.manga_planned"
    }
}