package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.dao.TitleDetailsDao
import com.g.pocketmal.data.database.model.TitleDetailsTable
import com.g.pocketmal.data.util.TitleType

class TitleDetailsDataSourceImpl(private val listDbStorage: ListDbStorage) : TitleDetailsDataSource {

    private val titleDetailsDao = listDbStorage.db.titleDetailsDao()

    override suspend fun getTitleDetailsById(id: Int, type: TitleType): TitleDetailsTable? {
        return titleDetailsDao.getDetailsById(id, type)
    }

    override suspend fun saveTitleDetails(titleDetails: TitleDetailsTable) {
        titleDetailsDao.insert(titleDetails)
    }
}
