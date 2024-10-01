package com.g.pocketmal.data.common

import android.util.SparseArray

import com.g.pocketmal.data.database.model.DbListRecord

class ListCounts {

    var inProgressCount = 0
        private set
    var completedCount = 0
        private set
    var onHoldCount = 0
        private set
    var droppedCount = 0
        private set
    var plannedCount = 0
        private set

    val generalCount: Int
        get() = inProgressCount + completedCount + onHoldCount + droppedCount + plannedCount

    constructor(list: SparseArray<DbListRecord>?) {

        if (list == null) {
            return
        }

        for (i in 0 until list.size()) {

            val item = list.valueAt(i) ?: continue

            when (item.myStatus) {
                Status.IN_PROGRESS -> inProgressCount++
                Status.COMPLETED -> completedCount++
                Status.ON_HOLD -> onHoldCount++
                Status.DROPPED -> droppedCount++
                Status.PLANNED -> plannedCount++
                Status.NOT_IN_LIST,
                Status.GENERAL -> {}
            }
        }
    }

    constructor(inProgressCount: Int, completedCount: Int, onHoldCount: Int, droppedCount: Int, plannedCount: Int) {
        this.inProgressCount = inProgressCount
        this.completedCount = completedCount
        this.onHoldCount = onHoldCount
        this.droppedCount = droppedCount
        this.plannedCount = plannedCount
    }
}
