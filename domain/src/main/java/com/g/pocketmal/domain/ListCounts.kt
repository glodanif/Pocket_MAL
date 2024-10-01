package com.g.pocketmal.domain

data class ListCounts(
    val inProgressCount: Int = 0,
    val completedCount: Int = 0,
    val onHoldCount: Int = 0,
    val droppedCount: Int = 0,
    val plannedCount: Int = 0,
    val generalCount: Int = 0,
)
