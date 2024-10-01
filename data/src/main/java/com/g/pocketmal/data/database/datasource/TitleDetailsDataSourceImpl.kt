package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.model.TitleDetailsTable

class TitleDetailsDataSourceImpl(private val listDbStorage: ListDbStorage) : TitleDetailsDataSource {

    private val titleDetailsDao = listDbStorage.db.titleDetailsDao()

    override suspend fun getTitleDetailsById(id: Int, type: DataTitleType): TitleDetailsTable {
        return titleDetailsDao.getDetailsById(id, type)
    }

    override suspend fun saveTitleDetails(titleDetails: TitleDetailsTable) {
        titleDetailsDao.insert(titleDetails)
    }
}
