package com.g.pocketmal.domain

import com.g.pocketmal.data.common.RecordsSubList

data class RecordLists(
    val inProgress: RecordsSubList = RecordsSubList(),
    val completed: RecordsSubList = RecordsSubList(),
    val onHold: RecordsSubList = RecordsSubList(),
    val dropped: RecordsSubList = RecordsSubList(),
    val planned: RecordsSubList = RecordsSubList(),
) {

    override fun toString(): String {
        return "RecordLists(inProgress=${inProgress.list.size}, completed=$${completed.list.size}, onHold=$${onHold.list.size}, dropped=$${dropped.list.size}, planned=$${planned.list.size})"
    }
}
