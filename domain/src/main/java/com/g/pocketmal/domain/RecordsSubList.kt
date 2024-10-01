package com.g.pocketmal.domain

import com.g.pocketmal.domain.entity.ListRecord

data class RecordsSubList(
    val list: List<ListRecord> = emptyList(),
    val quantity: Int = 0,
)
