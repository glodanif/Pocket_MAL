package com.g.pocketmal.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.g.pocketmal.R
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.databinding.FragmentEntityListBinding
import com.g.pocketmal.ui.ListActivity
import com.g.pocketmal.ui.adapter.EntityAdapter
import com.g.pocketmal.util.Action
import com.g.pocketmal.util.EpisodeType
import com.g.pocketmal.ui.viewmodel.RecordListViewModel
import com.google.android.material.snackbar.Snackbar

class EntityListFragment : Fragment() {

    private lateinit var binding: FragmentEntityListBinding

    private var fadeInAnimation: Animation? = null

    private lateinit var activity: ListActivity
    private lateinit var adapter: EntityAdapter

    private var listener: ListEventsListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity() as ListActivity

        adapter = EntityAdapter(activity)
        adapter.incrementListener = { id, newValue, titleType, episodeType, actionType ->
            listener?.onUpdate(id, titleType, episodeType, actionType, newValue)
        }

        fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentEntityListBinding.inflate(inflater, container, false)

        binding.srlPullToRefresh.setColorSchemeResources(R.color.main, R.color.main_light, R.color.main_dark)
        binding.srlPullToRefresh.setOnRefreshListener {
                listener?.onRefresh()
        }

        binding.gvEntityList.adapter = adapter
        binding.gvEntityList.setOnItemClickListener { _, _, _, id ->
            listener?.onItemClick(id.toInt())
        }

        return binding.root
    }

    override fun onDestroyView() {
        activity.hideProgressDialog()
        super.onDestroyView()
    }

    fun setListEventsListener(listener: ListEventsListener) {
        this.listener = listener
    }

    fun refreshAdapter(filterText: String?) {
        if (isAdded) {
            adapter.notifyDataSetChanged()
            applyFilter(filterText)
        }
    }

    fun applyFilter(searchPhrase: String?) {
        if (!searchPhrase.isNullOrEmpty()) {
            adapter.filter.filter(searchPhrase)
        } else {
            adapter.setCurrentList(adapter.originalTitles)
            adapter.notifyDataSetChanged()
        }
    }

    protected fun showEmptyListHeader(statusLabel: String) {
        if (isAdded) {
            binding.rlEmptyHolder.visibility = View.VISIBLE
            binding.tvEmptyText.text = getString(R.string.someListIsEmpty, statusLabel)
        }
    }

    fun displayList(list: List<RecordListViewModel>, editable: Boolean, simple: Boolean, withTags: Boolean) {

        if (!isAdded) {
            return
        }

        adapter.simpleView = simple
        adapter.withTags = withTags

        activity.hideFailMessage()
        binding.gvEntityList.visibility = View.VISIBLE
        binding.tvOfflineHeader.visibility = View.GONE

        adapter.setOriginalList(list)
        adapter.setActionsEnabled(editable)
        refreshAdapter(null)

        binding.rlEmptyHolder.visibility = View.GONE
    }

    fun displayEmptyList(label: String) {
        showEmptyListHeader(label)
    }

    fun onSyncStart(showDialog: Boolean) {

        if (!isAdded) {
            return
        }

        if (!showDialog) {
            binding.srlPullToRefresh.post { binding.srlPullToRefresh.isRefreshing = true }
        } else {
            activity.showProgressDialog(getString(R.string.synchronization))
        }

        activity.hideFailMessage()
        binding.gvEntityList.visibility = View.VISIBLE
    }

    fun onSyncFinish() {
        activity.hideProgressDialog()
        binding.srlPullToRefresh.post { binding.srlPullToRefresh.isRefreshing = false }
    }

    fun onSyncFailed(lastSync: String, @StringRes message: Int) {

        onSyncFinish()

        if (isAdded) {
            if (adapter.originalTitles.isNotEmpty()) {
                binding.tvOfflineHeader.text = getString(R.string.lastSynchronized, lastSync)
                binding.tvOfflineHeader.visibility = View.VISIBLE
                val snackBar = Snackbar.make(binding.gvEntityList, message, Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction(R.string.tryAgain) {
                    listener?.onRefresh()
                }
                snackBar.show()
            } else {
                activity.showFailMessage()
                binding.gvEntityList.visibility = View.GONE
            }
        }
    }

    fun showFailedLayout(lastSync: String) {
        binding.tvOfflineHeader.text = getString(R.string.lastSynchronized, lastSync)
        binding.tvOfflineHeader.visibility = View.VISIBLE
    }

    fun hideEmpty() {
        binding.gvEntityList.visibility = View.VISIBLE
        binding.rlEmptyHolder.visibility = View.GONE
    }

    interface ListEventsListener {
        fun onItemClick(id: Int)
        fun onUpdate(id: Int, titleType: TitleType, episodeType: EpisodeType, action: Action, newValue: Int)
        fun onRefresh()
    }

    companion object {

        private const val EXTRA_TITLE_TYPE = "extra.title_type"


        fun newInstance(type: TitleType): EntityListFragment {

            val fragment = EntityListFragment()

            val bundle = Bundle()
            bundle.putInt(EXTRA_TITLE_TYPE, type.type)
            fragment.arguments = bundle

            return fragment
        }
    }
}
