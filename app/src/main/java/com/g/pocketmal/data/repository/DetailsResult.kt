package com.g.pocketmal.data.repository

import com.g.pocketmal.domain.entity.DetailsEntity
import com.g.pocketmal.domain.entity.ListRecordEntity

data class DetailsResult(
    val record: ListRecordEntity,
    val details: DetailsEntity,
)
