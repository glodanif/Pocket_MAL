package com.g.pocketmal.util.list

import androidx.annotation.StringRes

import com.g.pocketmal.R
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.util.TitleType

class DataInterpreter private constructor() {

    companion object {

        fun getMediaTypeLabelFromNetworkConst(networkConst: String): String {

            return when (networkConst) {
                "tv" -> "TV"
                "ova" -> "OVA"
                "movie" -> "Movie"
                "special" -> "Special"
                "ona" -> "ONA"
                "music" -> "Music"
                "manga" -> "Manga"
                "novel" -> "Novel"
                "one_shot" -> "One Shot"
                "doujinshi" -> "Doujinshi"
                "manhwa" -> "Manhwa"
                "manhua" -> "Manhua"
                "oel" -> "OEL"
                else -> "Unknown"
            }
        }

        fun getSeriesStatusFromNetworkConst(networkConst: String): String {

            return when (networkConst) {
                "finished_airing" -> "Finished"
                "currently_airing" -> "Airing"
                "not_yet_aired" -> "Not Yet Aired"
                "finished" -> "Finished"
                "currently_publishing" -> "Publishing"
                "not_yet_published" -> "Not Yet Published"
                else -> "Unknown"
            }
        }

        @StringRes
        fun getStatusById(status: Status, type: TitleType): Int {
            return if (type == TitleType.ANIME)
                getAnimeWatchingStatusById(status)
            else
                getMangaReadingStatusById(status)
        }

        @StringRes
        fun getTypeLabel(type: TitleType) =
                if (type == TitleType.ANIME) R.string.anime_list else R.string.manga_list

        @StringRes
        fun getAnimeWatchingStatusById(status: Status): Int {
            return when (status) {
                Status.IN_PROGRESS -> R.string.watching
                Status.COMPLETED -> R.string.completed
                Status.ON_HOLD -> R.string.onHold
                Status.DROPPED -> R.string.dropped
                Status.PLANNED -> R.string.planToWatch
                else -> R.string.unknown
            }
        }

        @StringRes
        fun getMangaReadingStatusById(status: Status): Int {
            return when (status) {
                Status.IN_PROGRESS -> R.string.reading
                Status.COMPLETED -> R.string.completed
                Status.ON_HOLD -> R.string.onHold
                Status.DROPPED -> R.string.dropped
                Status.PLANNED -> R.string.planToRead
                else -> R.string.unknown
            }
        }

        fun getGenreId(genre: String?, type: TitleType): Int {

            when (genre) {
                "Action" -> return 1
                "Adventure" -> return 2
                "Cars" -> return 3
                "Comedy" -> return 4
                "Dementia" -> return 5
                "Demons" -> return 6
                "Doujinshi" -> return 43
                "Drama" -> return 8
                "Ecchi" -> return 9
                "Fantasy" -> return 10
                "Game" -> return 11
                "Gender Bender" -> return 44
                "Harem" -> return 35
                "Hentai" -> return 12
                "Historical" -> return 13
                "Horror" -> return 14
                "Josei" -> return if (type == TitleType.ANIME) 43 else 42
                "Kids" -> return 15
                "Magic" -> return 16
                "Martial Arts" -> return 17
                "Mecha" -> return 18
                "Military" -> return 38
                "Music" -> return 19
                "Mystery" -> return 7
                "Parody" -> return 20
                "Police" -> return 39
                "Psychological" -> return 40
                "Romance" -> return 22
                "Samurai" -> return 21
                "School" -> return 23
                "Sci-Fi" -> return 24
                "Seinen" -> return if (type == TitleType.ANIME) 42 else 41
                "Shoujo" -> return 25
                "Shoujo Ai" -> return 26
                "Shounen" -> return 27
                "Shounen Ai" -> return 28
                "Slice of Life" -> return 36
                "Space" -> return 29
                "Sports" -> return 30
                "Super Power" -> return 31
                "Supernatural" -> return 37
                "Thriller" -> return 41
                "Vampire" -> return 32
                "Yaoi" -> return 33
                "Yuri" -> return 34
                else -> return -1
            }
        }

        fun getGenreName(id: Int, type: TitleType): String {

            when (id) {
                1 -> return "Action"
                2 -> return "Adventure"
                3 -> return "Cars"
                4 -> return "Comedy"
                5 -> return "Dementia"
                6 -> return "Demons"
                43 -> return if (type == TitleType.ANIME) "Josei" else "Doujinshi"
                8 -> return "Drama"
                9 -> return "Ecchi"
                10 -> return "Fantasy"
                11 -> return "Game"
                44 -> return "Gender Bender"
                35 -> return "Harem"
                12 -> return "Hentai"
                13 -> return "Historical"
                14 -> return "Horror"
                15 -> return "Kids"
                16 -> return "Magic"
                17 -> return "Martial Arts"
                18 -> return "Mecha"
                38 -> return "Military"
                19 -> return "Music"
                7 -> return "Mystery"
                20 -> return "Parody"
                39 -> return "Police"
                40 -> return "Psychological"
                22 -> return "Romance"
                21 -> return "Samurai"
                23 -> return "School"
                24 -> return "Sci-Fi"
                42 -> return "Seinen"
                25 -> return "Shoujo"
                26 -> return "Shoujo Ai"
                27 -> return "Shounen"
                28 -> return "Shounen Ai"
                36 -> return "Slice of Life"
                29 -> return "Space"
                30 -> return "Sports"
                31 -> return "Super Power"
                37 -> return "Supernatural"
                41 -> return "Thriller"
                32 -> return "Vampire"
                33 -> return "Yaoi"
                34 -> return "Yuri"
                else -> return "Unknown"
            }
        }
    }
}
