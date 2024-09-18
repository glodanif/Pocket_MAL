package com.g.pocketmal.ui.legacy

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.g.pocketmal.data.common.ListCounts
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.legacy.comparator.SortingType
import com.g.pocketmal.ui.about.AboutActivity
import com.g.pocketmal.ui.legacy.dialog.SortingDialog
import com.g.pocketmal.ui.legacy.fragment.EntityListFragment
import com.g.pocketmal.ui.legacy.fragment.LeftMenuFragment
import com.g.pocketmal.ui.legacy.presenter.ListPresenter
import com.g.pocketmal.ui.legacy.route.ListRoute
import com.g.pocketmal.ui.legacy.view.ListView
import com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel
import com.g.pocketmal.ui.legacy.viewentity.RecordViewModel
import com.g.pocketmal.ui.settings.SettingsActivity
import com.g.pocketmal.util.Action
import com.g.pocketmal.util.EpisodeType
import com.g.pocketmal.util.list.updaters.AnimeUpdatingFlow
import com.g.pocketmal.util.list.updaters.MangaUpdatingFlow
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ListActivity : SkeletonToolbarActivity(), ListView,
    ListRoute {

    private val extraType = "extra.title_type"
    private val extraStatus = "extra.status"
    private val extraFilter = "extra.filter"
    private val tagList = "tag.list"
    private val backStackLists = "back_stack.lists"

    private val presenter: ListPresenter by inject { parametersOf(this) }

    private val container: FrameLayout by bind(R.id.fl_content)
    private var drawerLayout: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null

    private lateinit var menuFragment: LeftMenuFragment

    private lateinit var listFragment: EntityListFragment

    private var snackBar: Snackbar? = null

    private val handler = Handler()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer, CUSTOM_ACTIVITY)
        drawerLayout = findViewById(R.id.dl_layout)

        menuFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_side_menu) as LeftMenuFragment
        menuFragment.statusListener = { status -> presenter.onNewStatus(status) }
        menuFragment.typeListener = { type -> presenter.swapLists(type) }

        drawerLayout?.let { layout ->
            drawerToggle = object : ActionBarDrawerToggle(this, layout, toolbar,
                R.string.app_name,
                R.string.app_name
            ) {

                override fun onDrawerClosed(view: View) {
                    super.onDrawerClosed(view)
                    menuFragment.onDrawerClosed()
                }

                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                    super.onDrawerSlide(drawerView, 0f)
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, 0f)
                }
            }
            drawerToggle?.let { toggle ->
                layout.addDrawerListener(toggle)
            }
            layout.fitsSystemWindows = false
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        if (savedInstanceState == null) {
            listFragment = EntityListFragment.newInstance(TitleType.ANIME)
            addFragment(listFragment)
        } else {
            listFragment = supportFragmentManager.findFragmentByTag(tagList) as EntityListFragment
        }

        listFragment.setListEventsListener(object : EntityListFragment.ListEventsListener {

            override fun onUpdate(id: Int, titleType: TitleType, episodeType: EpisodeType, action: Action, newValue: Int) = executeIfOnline {
                presenter.incrementEpisode(id, titleType, episodeType)
            }

            override fun onItemClick(id: Int) {
                presenter.goToDetails(id)
            }

            override fun onRefresh() {
                presenter.loadListFromNetwork()
            }
        })

        presenter.attach()
        presenter.attachUpdaters(
                animeUpdater = AnimeUpdatingFlow(this, getCurrentTheme()),
                mangaUpdater = MangaUpdatingFlow(this, getCurrentTheme())
        )
        if (savedInstanceState != null) {
            val state = ListView.State(
                status = Status.from(savedInstanceState.getString(extraStatus)),
                titleType = TitleType.from(savedInstanceState.getInt(extraType)),
                filter = savedInstanceState.getString(extraFilter)
            )
            presenter.restoreState(state)
        }

        if (intent.action != null && intent.action.equals(Intent.ACTION_VIEW)) {
            presenter.deepLink(intent.data.toString())
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle?.syncState()
        if (savedInstanceState == null) {
            presenter.localStorageRouter()
        } else {
            presenter.initLists()
        }
    }

    override fun onRestart() {
        super.onRestart()
        presenter.reloadList()
    }

    fun closeDrawer() = drawerLayout?.let { layout ->
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START)
        }
    }

    fun isDrawerMode() = drawerLayout != null

    override fun displayList(list: List<RecordListViewModel>, filter: String?, editable: Boolean, simple: Boolean, withTags: Boolean) {
        listFragment.displayList(list, editable, simple, withTags)
        if (filter.isNullOrEmpty()) {
            closeFilter()
        } else {
            openFilter(filter)
        }
    }

    override fun setupActionBar(@StringRes title: Int, @StringRes subTitle: Int) {
        supportActionBar?.title = getString(title)
        supportActionBar?.subtitle = getString(subTitle)
    }

    override fun displayEmptyList(@StringRes label: Int) {
        listFragment.displayEmptyList(getString(label))
    }

    override fun hideEmptyList() {
        listFragment.hideEmpty()
    }

    override fun displayCounts(counts: ListCounts) {
        menuFragment.setCounts(counts)
    }

    override fun setupDrawer(type: TitleType) {
        menuFragment.setupLayout(type)
    }

    override fun showLastSyncHeader(label: String) {
        listFragment.showFailedLayout(label)
    }

    override fun showRewatchedPopup(times: String, text: Int) {
        showToast(getString(text, times))
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.fragment_fade_in, 0)
                .add(R.id.fl_content, fragment, tagList)
                .addToBackStack(backStackLists)
                .commitAllowingStateLoss()
    }

    fun showFailMessage(errorMessage: String = getString(R.string.getListFailed)) {
        snackBar = Snackbar.make(container, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.tryAgain) { presenter.loadListFromNetwork() }
        snackBar?.show()
    }

    override fun showSync(showDialog: Boolean) {
        listFragment.onSyncStart(showDialog)
    }

    override fun showSyncFailed(lastSync: String, @StringRes message: Int) {
        listFragment.onSyncFinish()
        listFragment.onSyncFailed(lastSync, message)
    }

    override fun showSyncFailedBecauseListIsPrivate(errorMessage: String) {
        showFailMessage(errorMessage)
    }

    override fun hideSyncIndicator() {
        listFragment.onSyncFinish()
    }

    override fun showLinParsingError() {
        showToast(R.string.unableToParseLink)
    }

    override fun showAlreadyCompleted(body: Int) {
        Snackbar.make(container, body, Snackbar.LENGTH_LONG).show()
    }

    fun hideFailMessage() {
        snackBar?.dismiss()
    }

    override fun showLoadingDialog(text: Int) {
        showProgressDialog(text)
    }

    override fun hideLoadingDialog() {
        hideProgressDialog()
    }

    override fun showActions(viewModel: RecordViewModel, action: Action) {
        showActionsPopup(viewModel, action)
    }

    override fun showAllEpisodesAlreadyCompleted(text: Int) {
        showToast(text)
    }

    override fun showUpdatingFailure() {
        Toast.makeText(this, R.string.updateFailed, Toast.LENGTH_LONG).show()
    }

    override fun closeApp() {
        finish()
    }

    override fun displaySortingDialog(type: SortingType, reverse: Boolean) {
        SortingDialog(this, getCurrentTheme(), type, reverse,
            onSortType = { sortingType, reversed ->
                presenter.onNewSorting(sortingType, reversed)

            }
        ).show()
    }

    override fun redirectToDetailsScreen(id: Int, type: TitleType) {
        TitleDetailsActivity.start(this, id, type)
    }

    override fun redirectToSearchScreen(type: TitleType) {
        //SearchActivity.start(this, type)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        invalidateOptionsMenu()
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main_activity_actions, menu)

        val refresh = menu.findItem(R.id.action_refresh)

        val enoughSpace = resources.getBoolean(R.bool.isTablet) ||
                resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        refresh.setShowAsAction(if (enoughSpace)
            MenuItem.SHOW_AS_ACTION_ALWAYS else MenuItem.SHOW_AS_ACTION_NEVER
        )

        customizeSearchView(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun customizeSearchView(menu: Menu) {

        val searchItem = menu.findItem(R.id.action_filter)
        searchMenuItem = searchItem
        searchView = searchItem.actionView as SearchView
        searchView?.apply {
            imeOptions = DEFAULT_KEYS_DISABLE
            queryHint = getString(R.string.filter)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    presenter.onFilter(newText)
                    listFragment.applyFilter(newText)
                    return true
                }
            })

            isIconified = true
        }

        val closeButton = searchView?.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        closeButton?.setImageResource(R.drawable.ic_filter)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

        if (drawerLayout != null && drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            closeDrawer()
        } else if (searchView != null && !searchView!!.isIconified) {
            closeFilter()
        } else {
            presenter.redirectBack()
        }
    }

    private fun openFilter(filter: String) = handler.post {
        searchMenuItem?.expandActionView()
        searchView?.setQuery(filter, false)
        searchView?.isIconified = false
        listFragment.applyFilter(filter)
    }

    private fun closeFilter() {
        searchView?.setQuery("", false)
        searchView?.isIconified = true
        searchMenuItem?.collapseActionView()
        listFragment.applyFilter("")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (drawerToggle?.onOptionsItemSelected(item) == true) {
            return true
        }

        when (item.itemId) {
            R.id.action_search -> {
                presenter.goToSearch()
                return true
            }
            R.id.action_refresh -> {
                presenter.loadListFromNetwork()
                return true
            }
            R.id.action_sort -> {
                presenter.selectSorting()
                return true
            }
            R.id.action_settings -> {
                SettingsActivity.start(this)
                return true
            }
            R.id.action_about -> {
                AboutActivity.startActivity(this)
                return true
            }
            else -> return false
        }
    }

    //FIXME: move to presenter?
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(extraType, presenter.type.type)
        outState.putString(extraStatus, presenter.status.status)
        outState.putString(extraFilter, presenter.filter)
        super.onSaveInstanceState(outState)
    }
}