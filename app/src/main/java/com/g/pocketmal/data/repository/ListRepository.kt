package com.g.pocketmal.data.repository

import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.util.list.ListsManager

class ListRepository(
    private val recordStorage: RecordDataSource,
    private val listsManager: ListsManager,
) {

    suspend fun loadListFromDb() {
        val dbAnimeList = recordStorage.getRecordsByType(TitleType.ANIME)
        val dbMangaList = recordStorage.getRecordsByType(TitleType.MANGA)
        if (dbAnimeList != null) {
            listsManager.initAnimeLists(dbAnimeList)
        }
        if (dbMangaList != null) {
            listsManager.initMangaLists(dbMangaList)
        }
    }
}