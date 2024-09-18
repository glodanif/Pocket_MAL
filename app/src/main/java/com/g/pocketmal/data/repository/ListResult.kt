package com.g.pocketmal.data.repository

import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel
import com.g.pocketmal.ui.list.ListState

sealed class ListResult {
    data class RecordsList(val list: List<DbListRecord>) : ListResult()
    data class Error(val throwable: Throwable) : ListResult()
}
