package com.g.pocketmal.domain

data class SortingOptions(
    val type: SortingType = SortingType.TITLE,
    val reverse: Boolean = false,
)
