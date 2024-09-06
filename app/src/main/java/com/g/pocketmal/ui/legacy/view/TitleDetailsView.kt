package com.g.pocketmal.ui.legacy.view

import androidx.annotation.StringRes
import com.g.pocketmal.util.Action

interface TitleDetailsView: com.g.pocketmal.ui.legacy.view.BaseSessionView {

    fun showNotInListLayout()
    fun displayRecord(viewModel: com.g.pocketmal.ui.legacy.viewmodel.RecordViewModel)
    fun displayDetails(viewModel: com.g.pocketmal.ui.legacy.viewmodel.TitleDetailsViewModel, englishPrimary: Boolean)
    fun displayRecord(record: com.g.pocketmal.ui.legacy.viewmodel.RecordViewModel, details: com.g.pocketmal.ui.legacy.viewmodel.TitleDetailsViewModel, englishPrimary: Boolean)
    fun displayNewlyAddedTitle(record: com.g.pocketmal.ui.legacy.viewmodel.RecordViewModel)

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

    fun showActions(viewModel: com.g.pocketmal.ui.legacy.viewmodel.RecordViewModel, action: Action)

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

    fun externalLinksPatternChanged(pattern: String)
}