package com.g.pocketmal.ui.legacy

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.g.pocketmal.data.util.PartOfYear
import com.g.pocketmal.data.util.Season
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.ui.legacy.adapter.SeasonalAdapter
import com.g.pocketmal.ui.legacy.popup.SeasonsSelectorPopup
import com.g.pocketmal.ui.legacy.presenter.SeasonalPresenter
import com.g.pocketmal.ui.legacy.route.SeasonalRoute
import com.g.pocketmal.ui.legacy.view.SeasonalView
import com.g.pocketmal.ui.legacy.viewentity.SeasonalSectionViewModel
import com.g.pocketmal.util.AnimeSeason
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SeasonalActivity : SkeletonToolbarActivity(), SeasonalView,
    SeasonalRoute {

    private val seasonalList: RecyclerView by bind(R.id.rv_seasonal)
    private val progress: ProgressBar by bind(R.id.pb_loading)
    private val seasonSelector: LinearLayout by bind(R.id.ll_options)
    private val seasonLabel: TextView by bind(R.id.tv_season)
    private val yearLabel: TextView by bind(R.id.tv_year)
    private val emptySeasonLabel: TextView by bind(R.id.tv_empty_season)

    private val presenter: SeasonalPresenter by inject { parametersOf(this) }

    private val adapter = SeasonalAdapter()
    private lateinit var layoutManager: GridLayoutManager

    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seasonal, CUSTOM_ACTIVITY)

        adapter.listener = { item -> presenter.itemClick(item.id) }

        layoutManager = GridLayoutManager(this, calculateNoOfColumns())
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    SeasonalAdapter.TYPE_HEADER -> calculateNoOfColumns()
                    SeasonalAdapter.TYPE_ITEM -> 1
                    else -> 1
                }
            }
        }

        seasonalList.layoutManager = layoutManager
        seasonalList.adapter = adapter

        seasonSelector.setOnClickListener {
            presenter.pickAnotherSeason()
        }

        presenter.loadLatestSeason()
    }

    override fun openSeasonPicker(season: Season) {
        val popup = SeasonsSelectorPopup(this)
        popup.setData(season)
        popup.listener = { newYear, newSeason ->
            presenter.loadSeason(newYear, newSeason)
        }
        popup.show(seasonSelector)
    }

    override fun displaySeason(year: Int, partOfYear: PartOfYear) {
        seasonLabel.text = AnimeSeason.getSeasonText(partOfYear)
        yearLabel.text = year.toString()
    }


    override fun showProgress() {
        progress.visibility = View.VISIBLE
        seasonalList.visibility = View.GONE
        emptySeasonLabel.visibility = View.GONE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun askToWait() {
        showToast(R.string.seasonal__wait)
    }

    override fun displayFailPopup() {
        if (!isFinishing && !isDestroyed) {
            snackBar = Snackbar.make(
                seasonalList,
                R.string.seasonal__fail,
                Snackbar.LENGTH_INDEFINITE
            )
                    .setAction(R.string.tryAgain) { presenter.loadLatestSeason() }
            snackBar?.show()
        }
    }

    override fun hideFailPopup() {
        snackBar?.dismiss()
    }

    override fun openDetailsScreen(id: Int) {
        TitleDetailsActivity.start(this, id, TitleType.ANIME)
    }

    override fun displaySeasonalAnime(items: List<SeasonalSectionViewModel>) {
        seasonalList.visibility = View.VISIBLE
        emptySeasonLabel.visibility = View.GONE
        adapter.setItems(items)
        adapter.notifyDataSetChanged()
        layoutManager.scrollToPosition(0)
    }

    override fun showEmptySeason() {
        seasonalList.visibility = View.GONE
        progress.visibility = View.GONE
        emptySeasonLabel.visibility = View.VISIBLE
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        layoutManager.spanCount = calculateNoOfColumns()
        adapter.notifyDataSetChanged()
    }

    fun calculateNoOfColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val no = displayMetrics.widthPixels / resources.getDimensionPixelSize(R.dimen.seasonal_column_width)
        return if (no == 0) 1 else no
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SeasonalActivity::class.java))
        }
    }
}