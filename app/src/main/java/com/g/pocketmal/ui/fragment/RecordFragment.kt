package com.g.pocketmal.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.g.pocketmal.R
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.databinding.FragmentUserTitleDetailsBinding
import com.g.pocketmal.loadUrl
import com.g.pocketmal.ui.SkeletonActivity
import com.g.pocketmal.ui.popup.EpisodesPopup
import com.g.pocketmal.ui.popup.ListPopup
import com.g.pocketmal.ui.utils.CollapseAnimator
import com.g.pocketmal.ui.viewmodel.RecordViewModel
import com.g.pocketmal.ui.viewmodel.TitleDetailsViewModel

open class RecordFragment : Fragment() {

    private lateinit var binding: FragmentUserTitleDetailsBinding

    private var listener: RecordActionsListener? = null

    private var isTitleDetailsOpened = false

    private lateinit var scorePopup: ListPopup<Int>
    private lateinit var statusPopup: ListPopup<Status>
    private lateinit var episodesPopup: EpisodesPopup

    private lateinit var inputMethodManager: InputMethodManager

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentUserTitleDetailsBinding.inflate(inflater, container, false)
        inputMethodManager = requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.btnStatus.setOnClickListener {
            if (this::statusPopup.isInitialized) {
                statusPopup.show(it)
            }
        }
        binding.btnScore.setOnClickListener {
            if (this::statusPopup.isInitialized) {
                scorePopup.show(it)
            }
        }
        binding.ibEpisodes.setOnClickListener {
            if (this::statusPopup.isInitialized) {
                episodesPopup.show(it)
            }
        }

        binding.btnIncrementEpisodes.setOnClickListener {
            listener?.onPlusEpisodeClick()
        }

        binding.btnIncrementSubEpisodes.setOnClickListener {
            listener?.onPlusSubEpisodeClick()
        }

        binding.ivTitleDetails.setOnClickListener {
            expandTitleDetails()
        }
        binding.llSimpleTitleRow.setOnClickListener {
            expandTitleDetails()
        }

        binding.btnAddToMyList.setOnClickListener {
            listener?.onAddToList()
        }

        binding.ivSeriesPoster.setOnClickListener {
            listener?.onPosterClick()
        }

        binding.tvCopyButton.setOnClickListener {
            listener?.onCopyClick()
        }

        binding.tvShareButton.setOnClickListener {
            listener?.onShareClick()
        }

        binding.btnRe.setOnClickListener {
            listener?.onRewatchingClick()
        }

        binding.ivEditTags.setOnClickListener {
            binding.ivEditTags.visibility = View.GONE
            binding.ivSaveTags.visibility = View.VISIBLE
            binding.ivCancel.visibility = View.VISIBLE
            binding.tvTags.isEnabled = true
            binding.tvTags.requestFocus()
            inputMethodManager.showSoftInput(binding.tvTags, InputMethodManager.SHOW_IMPLICIT)
            binding.tvTags.setSelection(binding.tvTags.text.length)
        }

        binding.ivCancel.setOnClickListener {
            binding.ivEditTags.visibility = View.VISIBLE
            binding.ivSaveTags.visibility = View.GONE
            binding.ivCancel.visibility = View.GONE
            binding.tvTags.isEnabled = false
            binding.tvTags.setText("")
            listener?.onTagsEditCanceled()
        }

        binding.ivSaveTags.setOnClickListener {
            binding.ivEditTags.visibility = View.VISIBLE
            binding.ivSaveTags.visibility = View.GONE
            binding.ivCancel.visibility = View.GONE
            binding.tvTags.isEnabled = false
            listener?.onTagsSubmitted(binding.tvTags.text.toString())
        }

        return binding.root
    }

    fun setUpdateTitleListener(updateTitleListener: RecordActionsListener) {
        this.listener = updateTitleListener
    }

    fun setTags(tags: String) {
        binding.tvTags.setText(tags)
    }

    fun showTitleInfo(viewModel: RecordViewModel) {

        if (!isAdded) {
            return
        }

        binding.btnIncrementEpisodes.text = viewModel.shortIncrementEpisodesLabel

        if (!viewModel.withSubEpisodes) {
            binding.btnIncrementSubEpisodes.visibility = View.GONE
        } else {
            binding.btnIncrementSubEpisodes.text = viewModel.shortIncrementSubEpisodesLabel
        }

        if (viewModel.myStatus == Status.NOT_IN_LIST) {
            binding.flAddingPanel.visibility = View.VISIBLE
            binding.llEditingPanel.visibility = View.GONE
            binding.llTagsHolder.visibility = View.GONE
        } else {
            binding.flAddingPanel.visibility = View.GONE
            binding.llEditingPanel.visibility = View.VISIBLE
            binding.llTagsHolder.visibility = View.VISIBLE
        }

        binding.tvTitle.text = viewModel.seriesTitle
        bindPoster(viewModel.seriesImage)
        setupChangeableViews(viewModel)

        statusPopup = ListPopup.makeStatusPopup(requireActivity() as SkeletonActivity, viewModel.recordType)
        statusPopup.clickListener = { value ->
            listener?.onNewStatusSelected(value)
        }

        scorePopup = ListPopup.makeScorePopup(requireActivity() as SkeletonActivity)
        scorePopup.clickListener = { value ->
            listener?.onNewScoreSelected(value)
        }

        episodesPopup = EpisodesPopup(requireActivity() as SkeletonActivity, viewModel)
        episodesPopup.submitClickListener = { episodes, subEpisodes ->
            listener?.onUpdateEpisodes(episodes, subEpisodes)
        }
    }

    private fun bindPoster(url: String?) {

        if (TextUtils.isEmpty(url)) {
            binding.ivSeriesPoster.setBackgroundResource(R.color.poster_background)
            return
        }

        binding.ivSeriesPoster.loadUrl(url)
    }

    private fun setupChangeableViews(record: RecordViewModel) {

        binding.tvEpisodes.text = record.fullEpisodesLabel
        binding.btnScore.text = record.myScoreLabel
        if (record.myRe) {
            binding.btnStatus.visibility = View.GONE
            binding.btnRe.visibility = View.VISIBLE
            binding.btnRe.text = record.reLabel
        } else {
            binding.btnRe.visibility = View.GONE
            binding.btnStatus.visibility = View.VISIBLE
            binding.btnStatus.text = record.myStatusLabel
        }
        binding.tvTags.setText(record.myTags)
    }

    fun setupJustAddedLayout() {
        binding.flAddingPanel.visibility = View.GONE
        binding.llEditingPanel.visibility = View.VISIBLE
        activity?.invalidateOptionsMenu()
    }

    fun setupNotInListLayout() {
        binding.flAddingPanel.visibility = View.VISIBLE
        binding.llEditingPanel.visibility = View.GONE
        binding.llTagsHolder.visibility = View.GONE
        activity?.invalidateOptionsMenu()
    }

    fun setAlternativeTitles(titleDetails: TitleDetailsViewModel, englishPrimary: Boolean) {

        if (!isAdded) {
            return
        }

        val romajiTitle = titleDetails.title
        val englishTitle = titleDetails.englishTitle

        binding.tvTitle.text = if (englishPrimary && !englishTitle.isNullOrEmpty()) englishTitle else romajiTitle

        if (englishPrimary && !englishTitle.isNullOrEmpty()) {
            binding.tvEnglishTitles.visibility = View.VISIBLE
            binding.tvEnglishTitles.text = Html.fromHtml(getString(R.string.romajiTitle, romajiTitle))
        } else {
            if (!TextUtils.isEmpty(englishTitle)) {
                binding.tvEnglishTitles.visibility = View.VISIBLE
                binding.tvEnglishTitles.text = Html.fromHtml(getString(R.string.englishTitle, englishTitle))
            } else {
                binding.tvEnglishTitles.visibility = View.GONE
            }
        }

        val synonyms = titleDetails.synonyms
        if (!TextUtils.isEmpty(synonyms)) {
            binding.tvSynonymTitles.visibility = View.VISIBLE
            binding.tvSynonymTitles.text = Html.fromHtml(getString(R.string.synonyms, synonyms))
        } else {
            binding.tvSynonymTitles.visibility = View.GONE
        }

        val japaneseTitle = titleDetails.japaneseTitle
        if (!TextUtils.isEmpty(japaneseTitle)) {
            binding.tvJapaneseTitles.visibility = View.VISIBLE
            binding.tvJapaneseTitles.text = Html.fromHtml(getString(R.string.japaneseTitle, japaneseTitle))
        } else {
            binding.tvJapaneseTitles.visibility = View.GONE
        }
    }

    private fun expandTitleDetails() {

        if (isTitleDetailsOpened) {
            CollapseAnimator.collapseView(requireActivity(), binding.llDetailedTitleRow)
            binding.tvTitle.isSingleLine = true
            binding.ivTitleDetails.setImageResource(R.drawable.ic_baseline_expand_more_24)
        } else {
            CollapseAnimator.expandView(requireActivity(), binding.llDetailedTitleRow)
            binding.tvTitle.isSingleLine = false
            binding.ivTitleDetails.setImageResource(R.drawable.ic_baseline_clear_24)
        }
        isTitleDetailsOpened = !isTitleDetailsOpened
    }

    interface RecordActionsListener {
        fun onAddToList()
        fun onUpdateEpisodes(episodes: Int?, subEpisodes: Int?)
        fun onNewScoreSelected(score: Int)
        fun onNewStatusSelected(status: Status)
        fun onRewatchingClick()
        fun onCopyClick()
        fun onShareClick()
        fun onPosterClick()
        fun onPlusEpisodeClick()
        fun onPlusSubEpisodeClick()
        fun onTagsEditCanceled()
        fun onTagsSubmitted(tags: String)
    }

    companion object {

        fun newInstance(): RecordFragment {
            return RecordFragment()
        }
    }
}
