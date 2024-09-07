package com.g.pocketmal.ui.legacy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.annotation.StringRes
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.transformedArgument
import com.g.pocketmal.ui.legacy.presenter.RankedPresenter
import com.g.pocketmal.ui.legacy.route.RankedRoute
import com.g.pocketmal.ui.legacy.view.RankedView
import com.g.pocketmal.ui.legacy.viewentity.RankedItemViewModel
import com.g.pocketmal.ui.legacy.widget.LazyLoadFooter
import com.g.pocketmal.ui.utils.LazyLoadOnScrollListener
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class RankedActivity : SkeletonToolbarActivity(), RankedView,
    RankedRoute {

    private val topType by transformedArgument<Int, RankingType>(EXTRA_TOP_TYPE, RankingType.ALL) {
        RankingType.from(it)
    }
    private val titleType by transformedArgument<Int, TitleType>(EXTRA_TITLE_TYPE, TitleType.ANIME) {
        TitleType.from(it)
    }

    private val presenter: RankedPresenter by inject { parametersOf(topType, titleType, this) }

    private val baseList: ListView by bind(R.id.lv_base_list)

    private lateinit var footer: LazyLoadFooter
    private lateinit var adapter: com.g.pocketmal.ui.legacy.adapter.EntityTopAdapter

    private val scrollListener = object : LazyLoadOnScrollListener(TITLES_IN_BUNCH, LAZY_LOAD_OFFSET) {
        override fun onLoad(offset: Int) {
            presenter.loadTopWithOffset(offset)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entity_base, CHILD_ACTIVITY)

        footer = LazyLoadFooter(this)
        baseList.addFooterView(footer, null, false)

        adapter = com.g.pocketmal.ui.legacy.adapter.EntityTopAdapter(this)

        baseList.adapter = adapter
        baseList.setOnScrollListener(scrollListener)
        baseList.setOnItemClickListener { _, _, _, id ->
            presenter.itemClick(id)
        }

        presenter.attach()
        presenter.loadTopWithOffset(0)
    }

    override fun setToolbarTitle(@StringRes title: Int) {
        setActionBarTitle(getString(title))
    }

    override fun addItemsToList(items: List<RankedItemViewModel>) {
        scrollListener.notifyFinish(items.size)
        adapter.addList(items)
        adapter.notifyDataSetChanged()
    }

    override fun notifyLoadingFailure() {
        scrollListener.notifyFail()
    }

    override fun showProgressFooter() {
        footer.setProgressVisible(true)
    }

    override fun hideProgressFooter() {
        footer.setProgressVisible(false)
    }

    override fun displayFailPopup() {

        if (!isFinishing && !isDestroyed) {
            Snackbar.make(baseList, R.string.getTopFail, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.tryAgain) {
                        presenter.loadTopWithOffset(scrollListener.loadedCount)
                    }
                    .show()
        }
    }

    override fun openDetailsScreen(id: Int, titleType: TitleType) {
        TitleDetailsActivity.start(this, id, titleType)
    }

    companion object {

        private const val EXTRA_TOP_TYPE = "extra_top_type"
        private const val EXTRA_TITLE_TYPE = "extra_title_type"

        private const val TITLES_IN_BUNCH = 50
        private const val LAZY_LOAD_OFFSET = 5

        fun start(context: Context, topType: RankingType, titleType: TitleType) {
            val intent = Intent(context, RankedActivity::class.java)
                    .putExtra(EXTRA_TOP_TYPE, topType.type)
                    .putExtra(EXTRA_TITLE_TYPE, titleType.type)
            context.startActivity(intent)
        }
    }
}