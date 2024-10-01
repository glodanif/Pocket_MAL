package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.data.database.model.TitleDetailsTable

interface TitleDetailsDataSource {
    suspend fun getTitleDetailsById(id: Int, type: DataTitleType): TitleDetailsTable
    suspend fun saveTitleDetails(titleDetails: TitleDetailsTable)
}
