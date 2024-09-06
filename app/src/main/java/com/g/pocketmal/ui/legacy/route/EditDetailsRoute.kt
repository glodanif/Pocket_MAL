package com.g.pocketmal.ui.legacy.route

import com.g.pocketmal.util.DateType
import com.g.pocketmal.data.api.UpdateParams

interface EditDetailsRoute {
    fun close()
    fun openDatePicker(preset: Long, type: DateType)
    fun returnResult(params: UpdateParams?)
}