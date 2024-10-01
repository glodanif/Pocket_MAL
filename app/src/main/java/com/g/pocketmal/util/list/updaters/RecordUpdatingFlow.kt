package com.g.pocketmal.util.list.updaters

import com.g.pocketmal.domain.UpdateParameters
import com.g.pocketmal.domain.entity.ListRecord
import com.g.pocketmal.util.Action
import java.sql.Date
import java.text.SimpleDateFormat

//FIXME: Distribute responsibilities
abstract class RecordUpdatingFlow {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    var listener: ((Action, ListRecord, UpdateParameters) -> Unit)? = null

    abstract fun updateTitle(actionType: Action, record: ListRecord, params: UpdateParameters)

    fun updateTitleDirectly(actionType: Action, record: ListRecord, params: UpdateParameters) {
        executeUpdate(actionType, record, params)
    }

    protected fun executeUpdate(actionType: Action, record: ListRecord, params: UpdateParameters) {
        listener?.invoke(actionType, record, params)
    }

    protected fun getTodayDate(): String = dateFormatter.format(Date(System.currentTimeMillis()))
}
