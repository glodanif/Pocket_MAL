package com.g.pocketmal.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.g.pocketmal.R
import com.g.pocketmal.argument
import com.g.pocketmal.bind
import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.transformedArgument
import com.g.pocketmal.ui.dialog.YesNoDialog
import com.g.pocketmal.ui.fragment.DetailsFragment
import com.g.pocketmal.ui.fragment.RecordFragment
import com.g.pocketmal.ui.presenter.TitleDetailsPresenter
import com.g.pocketmal.ui.route.TitleDetailsRoute
import com.g.pocketmal.ui.view.TitleDetailsView
import com.g.pocketmal.ui.widget.EventScrollView
import com.g.pocketmal.util.Action
import com.g.pocketmal.ui.viewmodel.RecordViewModel
import com.g.pocketmal.ui.viewmodel.TitleDetailsViewModel
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.util.list.updaters.AnimeUpdatingFlow
import com.g.pocketmal.util.list.updaters.MangaUpdatingFlow
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class TitleDetailsActivity : SkeletonToolbarActivity(), TitleDetailsView, TitleDetailsRoute {

    private val recordId: Int by argument(EXTRA_ID, 0)
    private val titleType by transformedArgument<Int, TitleType>(EXTRA_TITLE_TYPE, TitleType.ANIME) {
        TitleType.from(it)
    }
    private val presenter: TitleDetailsPresenter by inject {
        parametersOf(recordId, titleType, this)
    }

    private val container: LinearLayout by bind(R.id.ll_container)
    private val scrollView: EventScrollView by bind(R.id.esv_content)
    private val pullToRefresh: SwipeRefreshLayout by bind(R.id.srl_pull_to_refresh)
    private val noConnection: LinearLayout by bind(R.id.ll_no_connection)
    private val synopsisLabel: TextView by bind(R.id.tv_synopsis)
    private val topBar: LinearLayout? by bind(R.id.ll_top_bar)

    private lateinit var recordFragment: RecordFragment
    private lateinit var detailsFragment: DetailsFragment

    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState

        val layout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInMultiWindowMode)
            R.layout.activity_details_multiwindow else R.layout.activity_details

        setContentView(layout, CUSTOM_ACTIVITY)

        topBar?.let {
            scrollView.setOnScrollListener { _, _, y, _, _ ->
                it.translationY = -y / 2.5f
            }
        }

        pullToRefresh.setColorSchemeResources(R.color.main, R.color.main_light, R.color.main_dark)
        pullToRefresh.setOnRefreshListener {
            presenter.loadDetails(networkUpdate = true, retry = true)
        }

        findViewById<Button>(R.id.btn_retry).setOnClickListener {
            presenter.loadDetails(networkUpdate = true, retry = true)
        }

        setActionBarTitle(getString(if (titleType == TitleType.ANIME)
            R.string.animeDetailsTitle else R.string.mangaDetailsTitle))

        /*findViewById<View>(R.id.tv_go_to_website).setOnClickListener {
            presenter.onMalLinkClick()
        }*/

        setupFragments()

        val hasPreviousState = savedInstanceState != null
        if (hasPreviousState) {
            presenter.notLoadedAnythingLastTime =
                    savedInstanceState?.getBoolean(EXTRA_FAILED_PREVIOUSLY) ?: false
        }

        presenter.attachUpdaters(
                animeUpdater = AnimeUpdatingFlow(this, getCurrentTheme()),
                mangaUpdater = MangaUpdatingFlow(this, getCurrentTheme())
        )

        Handler().post {
            presenter.loadRecord(networkUpdate = !hasPreviousState)
        }
    }

    override fun showDetailsLoading() {
        detailsFragment.showLoading()
    }

    override fun hideDetailsLoading() {
        detailsFragment.hideLoading()
    }

    override fun showNotInListLayout() {
        recordFragment.setupNotInListLayout()
    }

    override fun displayRecord(viewModel: RecordViewModel) {
        recordFragment.showTitleInfo(viewModel)
    }

    override fun displayDetails(viewModel: TitleDetailsViewModel, englishPrimary: Boolean) {
        setActionBarTitle(viewModel.screenTitle)
        detailsFragment.bindDetails(viewModel)
        detailsFragment.recommendationClickListener = {
            presenter.onRecommendationsClick()
        }
        recordFragment.setAlternativeTitles(viewModel, englishPrimary)
        bindSynopsis(viewModel)
    }

    override fun displayRecord(record: RecordViewModel, details: TitleDetailsViewModel, englishPrimary: Boolean) {
        recordFragment.showTitleInfo(record)
        recordFragment.setAlternativeTitles(details, englishPrimary)
    }

    override fun displayNewlyAddedTitle(record: RecordViewModel) {
        recordFragment.showTitleInfo(record)
        recordFragment.setupJustAddedLayout()
    }

    override fun setTags(tags: String) {
        recordFragment.setTags(tags)
    }

    override fun showRemovingFailure() {
        showToast(R.string.removingFailed)
    }

    override fun showAddingFailure() {
        showMessageDialog(R.string.addingFailed)
    }

    override fun showAddingFailureBecauseNotApproved() {
        showMessageDialog(R.string.possiblyNotApprovedTitle)
    }

    override fun showUpdatingFailure() {
        showToast(R.string.updateFailed)
    }

    override fun showUnsynchronizedFailure() {
        showToast(R.string.updateUnsynchFailed)
    }

    override fun resetToolbarMenu() {
        invalidateOptionsMenu()
    }

    override fun showNotLoggedIn() {
        showToast(R.string.notLoggedIn)
    }

    override fun showAllEpisodesAlreadyCompleted(@StringRes text: Int) {
        showToast(text)
    }

    override fun showActions(viewModel: RecordViewModel, action: Action) {
        showActionsPopup(viewModel, action)
    }

    override fun showRewatchedPopup(times: String, text: Int) {
        showToast(getString(text, times))
    }

    override fun showLoadingDialog(@StringRes text: Int) {
        showProgressDialog(text)
    }

    override fun hideLoadingDialog() {
        hideProgressDialog()
    }

    override fun hideSyncIndicator() {
        pullToRefresh.isRefreshing = false
    }

    override fun showFailedToUpdateTitleThroughNetwork() {
        showToast(R.string.unableToRefreshTitleDetails)
    }

    override fun openDetailsEditorForResult(id: Int, titleType: TitleType) {
        EditDetailsActivity.startActivityForResult(this, id, titleType, EDIT_DETAILS)
    }

    override fun openMalLink(link: String) {
        openLink(link)
    }

    override fun openRecommendations(id: Int, titleType: TitleType) {
        RecommendationsActivity.start(this, id, titleType)
    }

    override fun close() {
        finish()
    }

    override fun showCopied() {
        Toast.makeText(this, R.string.copiedToClipboard, Toast.LENGTH_LONG).show()
    }

    override fun unableToCopy() {
        Toast.makeText(this, R.string.emptyTitle, Toast.LENGTH_LONG).show()
    }

    override fun openPoster(url: String?) {
        PosterActivity.start(this, url)
    }

    private fun bindSynopsis(viewModel: TitleDetailsViewModel) {
        synopsisLabel.visibility = View.VISIBLE
        synopsisLabel.text = viewModel.synopsis
    }

    private fun setupFragments() {

        if (savedInstanceState == null) {
            recordFragment = RecordFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fl_stats_container, recordFragment, TAG_USER_DETAILS)
                    .commitAllowingStateLoss()
            detailsFragment = DetailsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fl_details_container, detailsFragment, TAG_GENERAL_DETAILS)
                    .commitAllowingStateLoss()
        } else {
            recordFragment = supportFragmentManager
                    .findFragmentByTag(TAG_USER_DETAILS) as RecordFragment
            detailsFragment = supportFragmentManager
                    .findFragmentByTag(TAG_GENERAL_DETAILS) as DetailsFragment
        }

        recordFragment.setUpdateTitleListener(object : RecordFragment.RecordActionsListener {

            override fun onNewScoreSelected(score: Int) = executeIfOnline {
                presenter.setNewScore(score)
            }

            override fun onNewStatusSelected(status: Status) = executeIfOnline {
                presenter.setNewStatus(status)
            }

            override fun onUpdateEpisodes(episodes: Int?, subEpisodes: Int?) = executeIfOnline {
                presenter.updateEpisodes(episodes, subEpisodes)
            }

            override fun onRewatchingClick() {
                presenter.editRewatching()
            }

            override fun onCopyClick() {
                presenter.copyTitleToClipboard()
            }

            override fun onShareClick() {
                presenter.shareTitle()
            }

            override fun onPosterClick() {
                presenter.showPoster()
            }

            override fun onPlusEpisodeClick() = executeIfOnline {
                presenter.incrementEpisode()
            }

            override fun onPlusSubEpisodeClick() = executeIfOnline {
                presenter.incrementSubEpisode()
            }

            override fun onAddToList() = executeIfOnline {
                presenter.addTitleToMyList()
            }

            override fun onTagsEditCanceled() {
                presenter.resetTags()
            }

            override fun onTagsSubmitted(tags: String) = executeIfOnline {
                presenter.saveNewTags(tags)
            }
        })
    }

    override fun showUnableToLoadAnything() {
        noConnection.visibility = View.VISIBLE
        container.visibility = View.GONE
    }

    override fun hideUnableToLoadAnything() {
        noConnection.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    override fun shareTitle(link: String) {
        shareText(link)
    }

    override fun openDiscussion(link: String) {
        openLink(link)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return if (!presenter.isInMyList()) {
            super.onCreateOptionsMenu(menu)
        } else {
            val inflater = menuInflater
            inflater.inflate(R.menu.details_activity_actions, menu)
            super.onCreateOptionsMenu(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_edit_details -> {
                presenter.onEditDetailsClick()
                true
            }
            R.id.action_remove -> {
                YesNoDialog(this, getCurrentTheme(), getString(R.string.removingConfirmation),
                        yesClick = {
                            executeIfOnline { presenter.removeFromMyList() }
                        }
                ).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_DETAILS) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val params = data.getSerializableExtra(EditDetailsActivity.EXTRA_UPDATE_PARAMS) as UpdateParams?
                if (params != null) {
                    presenter.updateTitle(Action.ACTION_NONE, params, directUpdate = true)
                }
            }
        }
    }

    //FIXME: move to presenter?
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(EXTRA_FAILED_PREVIOUSLY, presenter.notLoadedAnythingLastTime)
        super.onSaveInstanceState(outState)
    }

    companion object {

        private const val EXTRA_FAILED_PREVIOUSLY = "extra.failed_previously"

        private const val EXTRA_ID = "extra.id"
        private const val EXTRA_TITLE_TYPE = "extra.title_type"

        const val TAG_USER_DETAILS = "tag.user_details"
        const val TAG_GENERAL_DETAILS = "tag.general_details"

        const val EDIT_DETAILS = 101

        fun start(context: Activity, id: Int, type: TitleType) {
            val intent = Intent(context, TitleDetailsActivity::class.java)
                    .putExtra(EXTRA_ID, id)
                    .putExtra(EXTRA_TITLE_TYPE, type.type)
            context.startActivity(intent)
        }
    }
}
