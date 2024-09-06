package com.g.pocketmal.ui.legacy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.g.pocketmal.data.api.request.SearchParams
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.transformedArgument
import com.g.pocketmal.ui.legacy.adapter.SearchResultAdapter
import com.g.pocketmal.ui.legacy.presenter.SearchPresenter
import com.g.pocketmal.ui.legacy.route.SearchRoute
import com.g.pocketmal.ui.legacy.view.SearchView
import com.g.pocketmal.ui.legacy.viewmodel.SearchResultViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

typealias ToolbarSearchView = androidx.appcompat.widget.SearchView

class SearchActivity : SkeletonToolbarActivity(), SearchView, SearchRoute {

    private val emptyResult: TextView by bind(R.id.tv_empty_result)
    private val resultList: RecyclerView by bind(R.id.rv_result)
    private val progressBar: ProgressBar by bind(R.id.pb_loading)

    private val type by transformedArgument<Int, TitleType>(EXTRA_SEARCH_TYPE, TitleType.ANIME) {
        TitleType.from(it)
    }

    private val presenter: SearchPresenter by inject { parametersOf(type, this) }

    private var searchView: androidx.appcompat.widget.SearchView? = null

    private val adapter = SearchResultAdapter()
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private var isRunAsChild = false

    private var snackBar: Snackbar? = null

    private val filterTextWatcher = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(searchString: String): Boolean {
            presenter.search(searchString.trim { it <= ' ' })
            searchView?.clearFocus()
            return true
        }

        override fun onQueryTextChange(searchString: String): Boolean {
            return false
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search, CUSTOM_ACTIVITY)

        val searchParams = intent.getParcelableExtra<SearchParams>(EXTRA_SEARCH_PARAMS)
        isRunAsChild = searchParams != null
        setActionBarTitle(getString(if (type == TitleType.ANIME) R.string.animeSearch else R.string.mangaSearch))

        adapter.listener = { id -> presenter.openDetails(id) }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        resultList.layoutManager = layoutManager
        resultList.adapter = adapter
    }

    override fun getDarkTheme(): Int {
        return R.style.Theme_Mal_Dark_NativeDialog
    }

    override fun getBlackTheme(): Int {
        return R.style.Theme_Mal_Black_NativeDialog
    }

    override fun showQueryIsTooShortMessage() {
        showToast(R.string.queryIsTooShort)
    }

    override fun displaySearchResult(list: List<SearchResultViewModel>) {
        adapter.setItems(list)
        adapter.notifyDataSetChanged()
        resultList.visibility = View.VISIBLE
        layoutManager.scrollToPosition(0)
    }

    override fun displayEmptyResult() {
        adapter.setItems(ArrayList())
        adapter.notifyDataSetChanged()
        emptyResult.visibility = View.VISIBLE
        resultList.visibility = View.INVISIBLE
        progressBar.visibility = View.GONE
    }

    override fun showProgress() {
        emptyResult.visibility = View.GONE
        resultList.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        adapter.setFooterVisible(false)
        progressBar.visibility = View.GONE
        if (searchView != null) {
            searchView?.clearFocus()
        }
        hideKeyboard()
    }

    override fun showFailPopup() {

        if (!isFinishing && !isDestroyed) {
            snackBar = Snackbar.make(resultList, R.string.searchFailed, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.tryAgain) {
                    presenter.search(searchView?.query?.toString() ?: "")
                }
            snackBar?.show()
        }
    }

    override fun hideFailPopup() {
        snackBar?.dismiss()
    }

    override fun openDetailsScreen(id: Int, titleType: TitleType) {
        TitleDetailsActivity.start(this, id, titleType)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_activity_actions, menu)
        customizeSearchView(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun customizeSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        searchView = (searchItem.actionView as ToolbarSearchView).apply {
            setOnQueryTextListener(filterTextWatcher)
            queryHint = getString(R.string.search)
            setIconifiedByDefault(false)
            isIconified = false
            isFocusable = false
            if (isRunAsChild) {
                clearFocus()
            }
        }
    }

    companion object {

        private const val EXTRA_SEARCH_TYPE = "extra.search_type"
        private const val EXTRA_SEARCH_PARAMS = "extra.search_params"

        fun start(context: Context, type: TitleType, searchParams: SearchParams? = null) {
            val intent = Intent(context, SearchActivity::class.java)
                .putExtra(EXTRA_SEARCH_TYPE, type.type)
                .putExtra(EXTRA_SEARCH_PARAMS, searchParams)
            context.startActivity(intent)
        }
    }
}
