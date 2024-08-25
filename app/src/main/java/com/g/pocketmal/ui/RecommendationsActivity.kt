package com.g.pocketmal.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g.pocketmal.R
import com.g.pocketmal.argument
import com.g.pocketmal.bind
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.transformedArgument
import com.g.pocketmal.ui.adapter.RecommendationsAdapter
import com.g.pocketmal.ui.presenter.RecommendationsPresenter
import com.g.pocketmal.ui.route.RecommendationsRoute
import com.g.pocketmal.ui.view.RecommendationsView
import com.g.pocketmal.ui.viewmodel.RecommendedTitleViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class RecommendationsActivity : SkeletonToolbarActivity(), RecommendationsView, RecommendationsRoute {

    private val type by transformedArgument<Int, TitleType>(EXTRA_TYPE, TitleType.ANIME) {
        TitleType.from(it)
    }
    private val id by argument<Int>(EXTRA_ID)

    private val presenter: RecommendationsPresenter by inject { parametersOf(id, type, this) }

    private val recommendationsList: RecyclerView by bind(R.id.rv_recommendations)
    private val progress: ProgressBar by bind(R.id.pb_loading)
    private val noRecommendationsLabel: TextView by bind(R.id.tv_no_recommendations)

    private val adapter = RecommendationsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations, CHILD_ACTIVITY)

        recommendationsList.layoutManager = LinearLayoutManager(this)
        recommendationsList.adapter = adapter

        presenter.loadRecommendations()
    }

    override fun displayRecommendations(items: List<RecommendedTitleViewModel>) {
        recommendationsList.visibility = View.VISIBLE
        noRecommendationsLabel.visibility = View.GONE
        adapter.recommendations = items
        adapter.listener = { id ->
            presenter.itemClick(id)
        }
        adapter.notifyDataSetChanged()
    }

    override fun displayNoRecommendations() {
        recommendationsList.visibility = View.GONE
        noRecommendationsLabel.visibility = View.VISIBLE
    }

    override fun displayError() {
        if (!isFinishing && !isDestroyed) {
            Snackbar.make(recommendationsList, R.string.getRecommendationsFailed, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.tryAgain) { presenter.loadRecommendations() }
                    .show()
        }
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun openDetailsScreen(id: Int, titleType: TitleType) {
        TitleDetailsActivity.start(this, id, titleType)
    }

    companion object {

        private const val EXTRA_ID = "extra.id"
        private const val EXTRA_TYPE = "extra.type"

        fun start(context: Context, id: Int, type: TitleType) {
            val intent = Intent(context, RecommendationsActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            intent.putExtra(EXTRA_TYPE, type.type)
            context.startActivity(intent)
        }
    }
}