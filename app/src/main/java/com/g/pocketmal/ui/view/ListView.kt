package com.g.pocketmal.ui.view

import androidx.annotation.StringRes
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.common.ListCounts
import com.g.pocketmal.ui.viewmodel.RecordListViewModel
import com.g.pocketmal.ui.viewmodel.RecordViewModel
import com.g.pocketmal.util.Action
import com.g.pocketmal.data.common.Status

interface ListView: BaseSessionView {

    fun displayList(list: List<RecordListViewModel>, filter: String?, editable: Boolean, simple: Boolean, withTags: Boolean)
    fun setupActionBar(@StringRes title: Int, @StringRes subTitle: Int)
    fun displayEmptyList(@StringRes label: Int)
    fun setupDrawer(type: TitleType)
    fun hideEmptyList()
    fun displayCounts(counts: ListCounts)
    fun showLinParsingError()
    fun showLastSyncHeader(label: String)
    fun showSyncFailed(lastSync: String, @StringRes message: Int)
    fun showSyncFailedBecauseListIsPrivate(errorMessage: String)
    fun showSync(showDialog: Boolean)
    fun showAlreadyCompleted(@StringRes body: Int)
    fun hideSyncIndicator()

    //FIXME and others - presenters shouldn't know about R class
    fun showAllEpisodesAlreadyCompleted(@StringRes text: Int)

    fun showRewatchedPopup(times: String, @StringRes text: Int)
    fun showUpdatingFailure()
    fun showActions(viewModel: RecordViewModel, action: Action)

    fun showLoadingDialog(@StringRes text: Int)
    fun hideLoadingDialog()

    class State(
            val status: Status,
            val titleType: TitleType,
            val filter: String?
    )
}