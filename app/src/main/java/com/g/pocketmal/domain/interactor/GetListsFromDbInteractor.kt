package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.domain.entity.converter.ListRecordEntityConverter
import kotlinx.coroutines.withContext

class GetListsFromDbInteractor(
        private val recordStorage: RecordDataSource
) : BaseInteractor<Unit, GetListsFromDbInteractor.Result>() {

    override suspend fun execute(input: Unit): Result {
        val dbAnimeList = recordStorage.getRecordsByType(TitleType.ANIME)
        val dbMangaList = recordStorage.getRecordsByType(TitleType.MANGA)
        return Result(dbAnimeList, dbMangaList)
    }

    class Result(
            val animeList: List<DbListRecord>?,
            val mangaList: List<DbListRecord>?
    )
}
