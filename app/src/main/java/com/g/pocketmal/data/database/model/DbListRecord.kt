package com.g.pocketmal.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.common.Status

@Entity(tableName = "record")
class DbListRecord(
        @PrimaryKey(autoGenerate = true)
        val uid: Int,
        @ColumnInfo(name = "series_id")
        val seriesId: Int,
        @ColumnInfo(name = "series_title")
        val seriesTitle: String,
        @ColumnInfo(name = "series_english_title")
        val seriesEnglishTitle: String,
        @ColumnInfo(name = "series_type")
        val seriesType: String,
        @ColumnInfo(name = "series_episodes")
        val seriesEpisodes: Int,
        @ColumnInfo(name = "series_sub_episodes")
        val seriesSubEpisodes: Int,
        @ColumnInfo(name = "series_status")
        val seriesStatus: String,
        @ColumnInfo(name = "series_image")
        val seriesImage: String?,
        @ColumnInfo(name = "my_start_date")
        var myStartDate: String?,
        @ColumnInfo(name = "my_finish_date")
        var myFinishDate: String?,
        @ColumnInfo(name = "my_episodes")
        var myEpisodes: Int,
        @ColumnInfo(name = "my_sub_episodes")
        var mySubEpisodes: Int,
        @ColumnInfo(name = "my_score")
        var myScore: Int,
        @ColumnInfo(name = "my_status")
        var myStatus: Status,
        @ColumnInfo(name = "my_re")
        var myRe: Boolean,
        @ColumnInfo(name = "my_re_value")
        var myReValue: Int,
        @ColumnInfo(name = "my_re_times")
        var myReTimes: Int,
        @ColumnInfo(name = "my_last_updated")
        var myLastUpdated: Long,
        @ColumnInfo(name = "my_tags")
        var myTags: List<String>,
        @ColumnInfo(name = "my_comments")
        var myComments: String?,
        @ColumnInfo(name = "my_priority", defaultValue = "0")
        var myPriority: Int,
        @ColumnInfo(name = "title_type")
        val titleType: TitleType
)
