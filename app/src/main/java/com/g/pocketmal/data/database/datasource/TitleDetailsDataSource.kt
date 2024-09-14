package com.g.pocketmal.data.database.datasource

import com.g.pocketmal.data.database.model.TitleDetailsTable
import com.g.pocketmal.domain.TitleType

interface TitleDetailsDataSource {
    suspend fun getTitleDetailsById(id: Int, type: TitleType): TitleDetailsTable?
    suspend fun saveTitleDetails(titleDetails: TitleDetailsTable)
}