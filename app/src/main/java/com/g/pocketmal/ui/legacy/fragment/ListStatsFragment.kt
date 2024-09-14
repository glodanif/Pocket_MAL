package com.g.pocketmal.ui.legacy.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.g.pocketmal.R
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.databinding.FragmentListStatsBinding
import java.util.*

class ListStatsFragment : Fragment() {

    private lateinit var binding: FragmentListStatsBinding

    private var userData: com.g.pocketmal.ui.legacy.viewentity.UserProfileViewModel? = null

    private var activity: Activity? = null

    private var type: TitleType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.activity = getActivity()
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setUserData(userData: com.g.pocketmal.ui.legacy.viewentity.UserProfileViewModel, type: TitleType) {

        if (!isAdded) return

        this.userData = userData
        this.type = type

        val isAnime = type === TitleType.ANIME

        binding.tvTitle.setText(if (isAnime) R.string.anime else R.string.manga)
        binding.tvInProgressLabel.setText(if (isAnime) R.string.watching else R.string.reading)
        //binding.tvRecentButton.setText(if (isAnime) R.string.recentAnime else R.string.recentManga)
        //binding.tvListButton.setText(if (isAnime) R.string.anime_list else R.string.manga_list)
        binding.tvVolumes.visibility = if (isAnime) View.INVISIBLE else View.VISIBLE

        binding.tvDays.text = activity!!.getString(R.string.stats__days_spent,
                if (isAnime) userData.animeSpentDays else userData.mangaSpentDays)
        binding.tvMeanScore.text = activity!!.getString(R.string.stats__mean_score,
                if (isAnime) userData.animeMeanScore else userData.mangaMeanScore)
        binding.tvReTimes.text = requireActivity().getString(if (isAnime) R.string.stats__rewatched else R.string.stats__reread,
                if (isAnime) userData.animeRewatched else userData.mangaReread)
        binding.tvEpisodes.text = requireActivity().getString(if (isAnime) R.string.stats__episodes else R.string.stats__chapters,
                if (isAnime) userData.animeEpisodes else userData.mangaChapters)
        binding.tvVolumes.text = requireActivity().getString(R.string.stats__volumes, userData.mangaVolumes)

        val counts = if (isAnime) userData.animeCounts else userData.mangaCounts

        binding.tvTotalEntries.text = requireActivity().getString(R.string.stats__total_entries, counts.generalCount)
        binding.tvInProgress.text = counts.inProgressCount.toString()
        binding.tvCompleted.text = counts.completedCount.toString()
        binding.tvOnHold.text = counts.onHoldCount.toString()
        binding.tvDropped.text = counts.droppedCount.toString()
        binding.tvPlanned.text = counts.plannedCount.toString()

        val items = ArrayList<com.g.pocketmal.ui.legacy.widget.MultiProgressBar.ProgressItem>()
        items.add(
            com.g.pocketmal.ui.legacy.widget.MultiProgressBar.ProgressItem(0,
                counts.inProgressCount, resources.getColor(R.color.in_progress_graph)))
        items.add(
            com.g.pocketmal.ui.legacy.widget.MultiProgressBar.ProgressItem(1,
                counts.completedCount, resources.getColor(R.color.completed_graph)))
        items.add(
            com.g.pocketmal.ui.legacy.widget.MultiProgressBar.ProgressItem(2,
                counts.onHoldCount, resources.getColor(R.color.on_hold_graph)))
        items.add(
            com.g.pocketmal.ui.legacy.widget.MultiProgressBar.ProgressItem(3,
                counts.droppedCount, resources.getColor(R.color.dropped_graph)))
        items.add(
            com.g.pocketmal.ui.legacy.widget.MultiProgressBar.ProgressItem(4,
                counts.plannedCount, resources.getColor(R.color.planned_graph)))

        binding.mpbProgress.setProgresses(items)
    }

    fun setVisible(isVisible: Boolean) {
        val view = view
        if (view != null) {
            view.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    /*@OnClick(R.id.tv_recent_button)
    void recentActivity() {
        RecentActivity.start(getActivity(), userData.getName(), type);
    }

    @OnClick(R.id.tv_list_button)
    void viewList() {
        UserListActivity.start(getActivity(), userData.getName(), type);
    }*/
}
