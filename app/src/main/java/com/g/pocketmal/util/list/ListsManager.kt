package com.g.pocketmal.util.list

import android.util.SparseArray
import com.g.pocketmal.data.common.ListCounts
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.common.Status
import java.util.*

class ListsManager {

    private val generalAnimeList = SparseArray<DbListRecord>()
    private val generalMangaList = SparseArray<DbListRecord>()

    private var isAnimeListInitialized = false
    private var isMangaListInitialized = false

    private var isActualAnimeList = false
    private var isActualMangaList = false

    val animeCounts: ListCounts
        get() = ListCounts(generalAnimeList)

    val mangaCounts: ListCounts
        get() = ListCounts(generalMangaList)

    fun initAnimeLists(generalList: List<DbListRecord>?) {
        if (generalList != null && generalList.isNotEmpty()) {
            for (anime in generalList) {
                this.generalAnimeList.put(anime.seriesId, anime)
            }
            isAnimeListInitialized = true
        }
    }

    fun initMangaLists(generalList: List<DbListRecord>?) {
        if (generalList != null && generalList.isNotEmpty()) {
            for (manga in generalList) {
                this.generalMangaList.put(manga.seriesId, manga)
            }
            isMangaListInitialized = true
        }
    }

    fun clearInstance() {

        generalAnimeList.clear()
        generalMangaList.clear()

        isActualMangaList = false
        isActualAnimeList = false
        isMangaListInitialized = false
        isAnimeListInitialized = false
    }

    fun getListByStatus(status: Status, type: TitleType): List<DbListRecord> {
        return if (type == TitleType.ANIME) asAnimeList(status) else asMangaList(status)
    }

    fun getGeneralList(type: TitleType): List<DbListRecord> {
        return if (type == TitleType.ANIME) asAnimeList(Status.GENERAL) else asMangaList(Status.GENERAL)
    }

    fun getTitleFromGeneralList(id: Int, titleType: TitleType): DbListRecord? {
        return findTitle(id, titleType)
    }

    fun findTitle(id: Int, titleType: TitleType): DbListRecord? {
        return if (titleType == TitleType.ANIME) {
            generalAnimeList.get(id)
        } else {
            generalMangaList.get(id)
        }
    }

    fun getAnimeFromGeneralList(id: Int): DbListRecord {
        return generalAnimeList.get(id)
    }

    fun getMangaFromGeneralList(id: Int): DbListRecord {
        return generalMangaList.get(id)
    }

    fun remove(recordId: Int, titleType: TitleType) {
        if (titleType == TitleType.ANIME) {
            generalAnimeList.remove(recordId)
        } else {
            generalMangaList.remove(recordId)
        }
    }

    fun add(title: DbListRecord) {
        if (title.titleType == TitleType.ANIME) {
            generalAnimeList.put(title.seriesId, title)
        } else {
            generalMangaList.put(title.seriesId, title)
        }
    }

    fun isListInitialized(type: TitleType): Boolean {
        return if (type == TitleType.ANIME) isAnimeListInitialized else isMangaListInitialized
    }

    fun isActualList(type: TitleType): Boolean {
        return if (type == TitleType.ANIME) isActualAnimeList else isActualMangaList
    }

    fun setListIsActual(type: TitleType) {
        if (type == TitleType.ANIME) {
            this.isActualAnimeList = true
        } else {
            this.isActualMangaList = true
        }
    }

    fun getCountsByType(type: TitleType): ListCounts {
        return if (type == TitleType.ANIME) animeCounts else mangaCounts
    }

    private fun asMangaList(status: Status): List<DbListRecord> {

        val arrayList = ArrayList<DbListRecord>(generalMangaList.size())

        for (i in 0 until generalMangaList.size()) {

            val manga = generalMangaList.valueAt(i)

            if (status == Status.GENERAL) {
                arrayList.add(manga)
            } else {
                if (manga.myStatus == status || (status == Status.IN_PROGRESS && manga.myRe)) {
                    arrayList.add(generalMangaList.valueAt(i))
                }
            }
        }
        return arrayList
    }

    private fun asAnimeList(status: Status): List<DbListRecord> {

        val arrayList = ArrayList<DbListRecord>(generalAnimeList.size())

        for (i in 0 until generalAnimeList.size()) {

            val anime = generalAnimeList.valueAt(i)

            if (status == Status.GENERAL) {
                arrayList.add(anime)
            } else {
                if (anime.myStatus == status || (status == Status.IN_PROGRESS && anime.myRe)) {
                    arrayList.add(generalAnimeList.valueAt(i))
                }
            }
        }
        return arrayList
    }
}
