package com.g.pocketmal.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.g.pocketmal.data.common.ListCounts

@Entity(tableName = "user_profile")
data class UserProfileTable(
        @PrimaryKey
        val id: Int,
        val name: String,
        val avatar: String?,
        val gender: String?,
        val birthday: String?,
        val location: String?,
        @ColumnInfo(name = "join_date")
        val joinDate: Long,
        @ColumnInfo(name = "anime_spent_days")
        val animeSpentDays: Float,
        @ColumnInfo(name = "anime_counts")
        val animeCounts: ListCounts,
        @ColumnInfo(name = "anime_mean_score")
        val animeMeanScore: Float,
        @ColumnInfo(name = "anime_episodes")
        val animeEpisodes: Int,
        @ColumnInfo(name = "anime_rewatched")
        val animeRewatched: Int,
        @ColumnInfo(name = "manga_spent_days")
        val mangaSpentDays: Float,
        @ColumnInfo(name = "manga_counts")
        val mangaCounts: ListCounts,
        @ColumnInfo(name = "manga_mean_score")
        val mangaMeanScore: Float,
        @ColumnInfo(name = "manga_chapters")
        val mangaChapters: Int,
        @ColumnInfo(name = "manga_volumes")
        val mangaVolumes: Int,
        @ColumnInfo(name = "manga_reread")
        val mangaReread: Int,
        @ColumnInfo(name = "time_zone")
        val timeZone: String?,
        @ColumnInfo(name = "is_supporter")
        val isSupporter: Boolean
)
