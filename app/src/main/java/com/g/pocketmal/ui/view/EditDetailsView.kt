package com.g.pocketmal.ui.view

import androidx.annotation.StringRes
import com.g.pocketmal.ui.viewmodel.RecordViewModel

interface EditDetailsView {

    fun showStartDate(label: String)
    fun showUnknownStart()
    fun showFinishDate(label: String)
    fun showUnknownFinish()
    //FIXME
    fun setupHeaders(@StringRes inProgressLabel: Int, @StringRes reTimesLabel: Int)
    fun setupReLayout(record: RecordViewModel)
    fun setupReChangeableInfo(isRe: Boolean, reTimes: String, episodes: String, subEpisodes: String)
    fun setEpisodes(episodes: String, subEpisodes: String)
    fun showEnteredValuesNotValid()
    fun showLoadingFail()

    class State(
            val startDate: String?,
            val finishDate: String?,
            val isRe: Boolean,
            val reTimes: String,
            val episodes: String,
            val subEpisodes: String
    )
}