package com.g.pocketmal.ui.legacy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.g.pocketmal.R
import com.g.pocketmal.data.common.ListCounts
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.keyvalue.SessionStorage
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.databinding.FragmentDrawerBinding
import com.g.pocketmal.domain.entity.BaseUserEntity
import com.g.pocketmal.loadUrl
import com.g.pocketmal.ui.browse.BrowseActivity
import com.g.pocketmal.ui.legacy.ListActivity
import com.g.pocketmal.ui.ranked.RankedActivity
import com.g.pocketmal.ui.seasonal.SeasonalActivity
import com.g.pocketmal.ui.userprofile.UserProfileActivity
import com.g.pocketmal.ui.utils.CircleTransform
import com.g.pocketmal.ui.utils.CollapseAnimator

class LeftMenuFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentDrawerBinding

    private lateinit var activity: ListActivity
    var statusListener: ((Status) -> Unit)? = null
    var typeListener: ((TitleType) -> Unit)? = null

    private var titleType = TitleType.ANIME

    private var clickedView: View? = null

    private lateinit var sessionStorage: SessionStorage

    private var currentUser: BaseUserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionStorage = SessionStorage(requireContext())
        currentUser = sessionStorage.user
        this.activity = requireActivity() as ListActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentDrawerBinding.inflate(inflater, container, false)

        binding.rlInProgressHolder.setOnClickListener(this)
        binding.rlCompletedHolder.setOnClickListener(this)
        binding.rlOnHoldHolder.setOnClickListener(this)
        binding.rlDroppedHolder.setOnClickListener(this)
        binding.rlPlannedHolder.setOnClickListener(this)

        binding.tvFriends.setOnClickListener(this)
        binding.tvPeopleSearch.setOnClickListener(this)
        binding.rlUserInfo.setOnClickListener(this)
        binding.tvTopButton.setOnClickListener(this)
        binding.tvMostPopularButton.setOnClickListener(this)
        binding.tvUpcomingButton.setOnClickListener(this)
        binding.tvHistoryButton.setOnClickListener(this)
        binding.tvAiringButton.setOnClickListener(this)
        binding.tvSeasonalButton.setOnClickListener(this)
        binding.rlSwitch.setOnClickListener(this)

        binding.rlBase.setOnClickListener {
            if (binding.llBaseHolder.visibility == View.VISIBLE) {
                CollapseAnimator.collapseView(activity, binding.llBaseHolder)
            } else {
                CollapseAnimator.expandView(activity, binding.llBaseHolder) {
                    binding.svContainer.smoothScrollTo(0, binding.rlBase.top)
                }
            }
        }

        bindUser()

        return binding.root
    }

    private fun bindUser() = currentUser?.let { user ->

        binding.ivShareList.setOnClickListener {
            val res = if (titleType == TitleType.ANIME)
                R.string.shareAnimeListText else R.string.shareMangeListText
            activity.shareText(activity.getString(res, user.name))
        }

        binding.tvUsername.text = user.name

        binding.ivAvatar.loadUrl(
                url = user.picture,
                placeholder = R.drawable.empty_avatar,
                transformation = CircleTransform()
        )
    }

    override fun onClick(view: View) {
        if (activity.isDrawerMode()) {
            activity.closeDrawer()
            clickedView = view
        } else {
            handleClick(view.id)
            clickedView = null
        }
    }

    fun onDrawerClosed() {
        if (clickedView != null) {
            handleClick(clickedView!!.id)
            clickedView = null
        }
    }

    private fun handleClick(id: Int) {

        when (id) {
            R.id.rl_switch ->
                typeListener?.invoke(if (titleType == TitleType.ANIME) TitleType.MANGA else TitleType.ANIME)
            R.id.tv_top_button -> RankedActivity.start(activity, RankingType.ALL, titleType)
            R.id.tv_most_popular_button -> RankedActivity.start(activity, RankingType.BY_POPULARITY, titleType)
            R.id.tv_upcoming_button -> BrowseActivity.start(activity, RankingType.UPCOMING, titleType)
            R.id.tv_airing_button -> BrowseActivity.start(activity, RankingType.AIRING, titleType)
            R.id.rl_user_info -> currentUser?.let { user ->
                UserProfileActivity.start(activity, user.id)
            }
            R.id.rl_in_progress_holder -> statusListener?.invoke(Status.IN_PROGRESS)
            R.id.rl_completed_holder -> statusListener?.invoke(Status.COMPLETED)
            R.id.rl_on_hold_holder -> statusListener?.invoke(Status.ON_HOLD)
            R.id.rl_dropped_holder -> statusListener?.invoke(Status.DROPPED)
            R.id.rl_planned_holder -> statusListener?.invoke(Status.PLANNED)
            R.id.tv_seasonal_button -> SeasonalActivity.start(activity)
        }
    }

    fun setCounts(counts: ListCounts) {
        binding.tvTotalCount.text = counts.generalCount.toString()
        binding.tvInProgressCount.text = counts.inProgressCount.toString()
        binding.tvCompletedCount.text = counts.completedCount.toString()
        binding.tvOnHoldCount.text = counts.onHoldCount.toString()
        binding.tvDroppedCount.text = counts.droppedCount.toString()
        binding.tvPlannedCount.text = counts.plannedCount.toString()
    }

    fun setupLayout(type: TitleType) {

        this.titleType = type

        val isAnime = type == TitleType.ANIME
        binding.tvSeasonalButton.visibility = if (isAnime) View.VISIBLE else View.GONE
        binding.vSeasonalDivider.visibility = if (isAnime) View.VISIBLE else View.GONE
        binding.llAnimeBrowse.visibility = if (isAnime) View.VISIBLE else View.GONE
        binding.tvAiringButton.setText(if (isAnime) R.string.airing else R.string.publishing)
        binding.tvInProgressLabel.setText(if (isAnime) R.string.watching else R.string.reading)
        binding.tvPlannedLabel.setText(if (isAnime) R.string.planToWatch else R.string.planToRead)
        binding.tvSwitchLabel.setText(if (isAnime) R.string.switchToManga else R.string.switchToAnime)
    }
}
