package com.g.pocketmal.domain.entity

import com.g.pocketmal.domain.TitleType

data class RelatedTitle(
    val id: Int,
    val name: String,
    val titleType: TitleType,
    val relationType: String,
)
