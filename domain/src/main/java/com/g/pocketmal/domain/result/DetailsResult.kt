package com.g.pocketmal.domain.result

import com.g.pocketmal.domain.entity.TitleDetails
import com.g.pocketmal.domain.entity.ListRecord

data class DetailsResult(
    val record: ListRecord,
    val details: TitleDetails,
)
