package com.g.pocketmal.data.common

import com.g.pocketmal.data.database.model.DbListRecord

data class RecordsSubList(
    val list: List<DbListRecord> = emptyList(),
    val quantity: Int = 0,
)
