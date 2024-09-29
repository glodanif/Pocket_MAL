package com.g.pocketmal.ui.legacy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.g.pocketmal.R
import com.g.pocketmal.data.database.model.RelatedTitle
import com.g.pocketmal.databinding.FragmentGeneralTitleDetailsBinding
import com.g.pocketmal.ui.legacy.TitleDetailsActivity
import com.g.pocketmal.ui.legacy.widget.StatsItem
import java.util.*

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentGeneralTitleDetailsBinding

    var recommendationClickListener: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGeneralTitleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun showLoading() {
        binding.llInfo.visibility = View.GONE
        binding.pbProgress.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.llInfo.visibility = View.VISIBLE
        binding.pbProgress.visibility = View.GONE
    }

    fun bindDetails(details: com.g.pocketmal.ui.legacy.viewentity.TitleDetailsViewModel) {

        if (!isAdded) return

        binding.tvScore.text = details.score

        binding.tvType.text = details.type
        binding.tvStatus.text = details.status

        binding.tvEpisodes.text = details.episodesLabel
        if (details.withSubEpisodes) {
            binding.tvSubEpisodes.visibility = View.VISIBLE
            binding.tvSubEpisodes.text = details.subEpisodesLabel
        } else {
            binding.tvSubEpisodes.visibility = View.GONE
        }

        binding.asvDatesStreak.setDates(details.startDate, details.finishDate)
        binding.tvScoredUsers.text = details.scoredUsersCount

        binding.llStatsHolder.removeAllViews()
        setupStatsUi(details)

        val airingStats = details.airingStats

        if (airingStats != null) {
            binding.tvBroadcast.visibility = View.VISIBLE
            binding.tvBroadcast.text = airingStats
        } else {
            binding.tvBroadcast.visibility = View.GONE
        }

        binding.tvScoredUsers.text = details.scoredUsersCount

        binding.rlRecommendations.setOnClickListener {
            recommendationClickListener?.invoke()
        }

        val views = ArrayList<com.g.pocketmal.ui.legacy.widget.GenreView>()
        for (genre in details.genres) {
            val genreView =
                com.g.pocketmal.ui.legacy.widget.GenreView(requireActivity(), genre.id, genre.name)
            views.add(genreView)
        }
        binding.glGenres.post { binding.glGenres.populateText(views) }

        binding.tvRanked.text = details.ranked

        val relatedTitles = details.relatedTitles

        if (relatedTitles.isEmpty()) {
            binding.llRelatedTitles.visibility = View.GONE
        } else {
            binding.llRelatedTitles.visibility = View.VISIBLE

            val titles = arrayOfNulls<TextView>(relatedTitles.size)

            binding.llRelatedTitlesContainer.removeAllViews()
            binding.llRelatedTitles.visibility = if (relatedTitles.isEmpty()) View.GONE else View.VISIBLE

            for (i in titles.indices) {
                titles[i] = getRelatedTitleView(relatedTitles[i]) {

                    val currentItem = it.tag as RelatedTitle

                    if (currentItem.id == RelatedTitle.UNKNOWN_ID) {
                        Toast.makeText(activity, R.string.details__unable_to_parse_link, Toast.LENGTH_LONG).show()
                    } else {
                        TitleDetailsActivity.start(requireActivity(), currentItem.id, currentItem.recordType)
                    }
                }
                binding.llRelatedTitlesContainer.addView(titles[i])
            }
        }
    }

    private fun setupStatsUi(item: com.g.pocketmal.ui.legacy.viewentity.TitleDetailsViewModel) {
        for (data in item.detailsList) {
            binding.llStatsHolder.addView(StatsItem(requireActivity(), data))
        }
    }

    private fun getRelatedTitleView(relatedTitleItem: RelatedTitle, clickListener: (View) -> Unit): TextView {

        val textView =
            com.g.pocketmal.ui.legacy.widget.RelatedTitleView(requireActivity(), relatedTitleItem)

        if (relatedTitleItem.isLink()) {
            textView.tag = relatedTitleItem
            textView.setOnClickListener { view -> clickListener.invoke(view) }
        }

        return textView
    }

    companion object {

        fun newInstance(): DetailsFragment {
            return DetailsFragment()
        }
    }
}
