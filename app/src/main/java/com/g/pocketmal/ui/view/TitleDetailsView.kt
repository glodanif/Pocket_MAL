package com.g.pocketmal.ui.view

import androidx.annotation.StringRes
import com.g.pocketmal.util.Action
import com.g.pocketmal.ui.viewmodel.RecordViewModel
import com.g.pocketmal.ui.viewmodel.TitleDetailsViewModel

interface TitleDetailsView: BaseSessionView {

    fun showNotInListLayout()
    fun displayRecord(viewModel: RecordViewModel)
    fun displayDetails(viewModel: TitleDetailsViewModel, englishPrimary: Boolean)
    fun displayRecord(record: RecordViewModel, details: TitleDetailsViewModel, englishPrimary: Boolean)
    fun displayNewlyAddedTitle(record: RecordViewModel)

    fun showRemovingFailure()
    fun showAddingFailure()
    fun showAddingFailureBecauseNotApproved()
    fun showUpdatingFailure()
    fun showUnsynchronizedFailure()
    fun showNotLoggedIn()
    fun showFailedToUpdateTitleThroughNetwork()

    fun resetToolbarMenu()

    //FIXME and others
    fun showAllEpisodesAlreadyCompleted(@StringRes text: Int)
    fun showRewatchedPopup(times: String, @StringRes text: Int)

    fun showActions(viewModel: RecordViewModel, action: Action)

    fun showLoadingDialog(@StringRes text: Int)
    fun hideLoadingDialog()
    fun hideSyncIndicator()

    fun showDetailsLoading()
    fun hideDetailsLoading()

    fun showUnableToLoadAnything()
    fun hideUnableToLoadAnything()

    fun setTags(tags: String)

    fun showCopied()
    fun unableToCopy()
}