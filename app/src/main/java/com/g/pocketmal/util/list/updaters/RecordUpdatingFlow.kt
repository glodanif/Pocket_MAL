package com.g.pocketmal.util.list.updaters

import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.util.Action
import java.sql.Date
import java.text.SimpleDateFormat

//FIXME: Distribute responsibilities
abstract class RecordUpdatingFlow {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    var listener: ((Action, DbListRecord, UpdateParams) -> Unit)? = null

    abstract fun updateTitle(actionType: Action, record: DbListRecord, params: UpdateParams)

    fun updateTitleDirectly(actionType: Action, record: DbListRecord, params: UpdateParams) {
        executeUpdate(actionType, record, params)
    }

    protected fun executeUpdate(actionType: Action, record: DbListRecord, params: UpdateParams) {
        listener?.invoke(actionType, record, params)
    }

    protected fun getTodayDate(): String = dateFormatter.format(Date(System.currentTimeMillis()))
}
