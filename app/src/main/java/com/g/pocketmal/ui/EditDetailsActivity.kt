package com.g.pocketmal.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.*
import androidx.annotation.StringRes
import com.g.pocketmal.*
import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.ui.dialog.ClearButtonDatePickerDialog
import com.g.pocketmal.ui.dialog.MessageDialog
import com.g.pocketmal.ui.presenter.EditDetailsPresenter
import com.g.pocketmal.ui.route.EditDetailsRoute
import com.g.pocketmal.ui.utils.NumberInputFilter
import com.g.pocketmal.ui.view.EditDetailsView
import com.g.pocketmal.util.DateType
import com.g.pocketmal.ui.viewmodel.RecordViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class EditDetailsActivity : SkeletonToolbarActivity(), EditDetailsView, EditDetailsRoute {

    private val recordId: Int by argument(EXTRA_RECORD_ID, 0)
    private val titleType: TitleType by transformedArgument<Int, TitleType>(EXTRA_TITLE_TYPE, TitleType.ANIME) {
        TitleType.from(it)
    }

    private val presenter: EditDetailsPresenter by inject { parametersOf(recordId, titleType, this) }

    private val periodLabel: TextView by bind(R.id.tv_period_label)
    private val startDateButton: TextView by bind(R.id.tv_start_date)
    private val finishDateButton: TextView by bind(R.id.tv_finish_date)

    private val rewatchingHolder: LinearLayout by bind(R.id.ll_rewatching_holder)
    private val rewatching: CheckBox by bind(R.id.cb_rewatching)
    private val rewatchingLabel: TextView by bind(R.id.tv_rewatching_label)
    private val rewatchingTimesLabel: TextView by bind(R.id.tv_rewatching_times_label)
    private val rewatchingTimesInput: EditText by bind(R.id.et_rewatching_times_input)

    private val episodesLabel: TextView by bind(R.id.tv_episodes_label)
    private val myEpisodesField: EditText by bind(R.id.et_episodes_field)
    private val seriesEpisodesLabel: TextView by bind(R.id.tv_series_episodes)
    private val subEpisodesLabel: TextView by bind(R.id.tv_sub_episodes_label)
    private val mySubEpisodesField: EditText by bind(R.id.et_sub_episodes_field)
    private val seriesSubEpisodesLabel: TextView by bind(R.id.tv_series_sub_episodes)

    private val subEpisodesHolder: LinearLayout by bind(R.id.ll_sub_episodes_holder)

    override fun getDarkTheme() = R.style.Theme_Mal_Dark_NativeDialog_DialogWhenLarge

    override fun getBlackTheme() = R.style.Theme_Mal_Black_NativeDialog_DialogWhenLarge

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInMultiWindowMode) {
            R.layout.activity_edit_details_multiwindow
        } else {
            R.layout.activity_edit_details
        }

        setContentView(layout, CHILD_ACTIVITY)

        if (savedInstanceState != null) {

            val state = EditDetailsView.State(
                    startDate = savedInstanceState.getString(EXTRA_STATE_START_DATE),
                    finishDate = savedInstanceState.getString(EXTRA_STATE_END_DATE),
                    isRe = savedInstanceState.getBoolean(EXTRA_STATE_IS_RE),
                    reTimes = savedInstanceState.getString(EXTRA_STATE_RE_TIMES) ?: "0",
                    episodes = savedInstanceState.getString(EXTRA_STATE_EPISODES) ?: "0",
                    subEpisodes = savedInstanceState.getString(EXTRA_STATE_SUB_EPISODES) ?: "0"
            )

            presenter.restoreState(state)
        }

        rewatchingTimesInput.filters = arrayOf<InputFilter>(NumberInputFilter(NumberInputFilter.MAX_REWATCHING_NUMBER))

        findViewById<Button>(R.id.btn_save).setOnClickListener {

            executeIfOnline {
                presenter.updateTitle(
                        isRe = rewatching.isChecked,
                        reTimes = rewatchingTimesInput.getTrimmedText(),
                        reEpisodes = myEpisodesField.getTrimmedText(),
                        reSubEpisodes = mySubEpisodesField.getTrimmedText()
                )
            }
        }

        findViewById<TextView>(R.id.tv_start_date).setOnClickListener {
            presenter.selectStartDate()
        }

        findViewById<TextView>(R.id.tv_finish_date).setOnClickListener {
            presenter.selectFinishDate()
        }

        presenter.setupLayout()
        presenter.loadRecord()
    }

    override fun openDatePicker(preset: Long, type: DateType) {
        ClearButtonDatePickerDialog.show(this, preset) { _, year, month, dayOfMonth ->
            presenter.setNewDate(type, year, month, dayOfMonth)
        }
    }

    override fun setupHeaders(@StringRes inProgressLabel: Int, @StringRes reTimesLabel: Int) {
        periodLabel.setText(inProgressLabel)
        rewatchingTimesLabel.setText(reTimesLabel)
    }

    override fun setupReLayout(record: RecordViewModel) {

        rewatchingHolder.visibility = View.VISIBLE
        subEpisodesHolder.visibility = if (record.withSubEpisodes) View.VISIBLE else View.GONE

        rewatchingLabel.text = record.reLabel
        rewatching.text = record.reLabel

        seriesEpisodesLabel.text = getString(R.string.series_episodes_label, record.seriesEpisodesLabel)
        episodesLabel.text = record.episodesTypeLabel

        if (record.withSubEpisodes) {
            seriesSubEpisodesLabel.text = getString(R.string.series_episodes_label, record.seriesSubEpisodesLabel)
            subEpisodesLabel.text = record.subEpisodesTypeLabel
        }

        rewatching.setOnCheckedChangeListener { _, isChecked ->
            presenter.setRewatching(isChecked)
        }
    }

    override fun setupReChangeableInfo(isRe: Boolean, reTimes: String, episodes: String, subEpisodes: String) {
        rewatching.isChecked = isRe
        rewatchingTimesInput.setText(reTimes)
        myEpisodesField.setText(episodes)
        mySubEpisodesField.setText(subEpisodes)
    }

    override fun setEpisodes(episodes: String, subEpisodes: String) {
        myEpisodesField.setText(episodes)
        mySubEpisodesField.setText(subEpisodes)
    }

    override fun showStartDate(label: String) {
        startDateButton.text = label
    }

    override fun showUnknownStart() {
        startDateButton.text = getString(R.string.unknown)
    }

    override fun showFinishDate(label: String) {
        finishDateButton.text = label
    }

    override fun showUnknownFinish() {
        finishDateButton.text = getString(R.string.unknown)
    }

    override fun showLoadingFail() {
        showToast(R.string.edit_details__title_not_found)
    }

    override fun showEnteredValuesNotValid() {
        MessageDialog(this, getCurrentTheme(), getString(R.string.edit_details__invalid_quantity)).show()
    }

    override fun close() {
        finish()
    }

    override fun returnResult(params: UpdateParams?) {
        setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_UPDATE_PARAMS, params))
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_STATE_START_DATE, presenter.startDate)
        outState.putString(EXTRA_STATE_END_DATE, presenter.finishDate)
        outState.putBoolean(EXTRA_STATE_IS_RE, rewatching.isChecked)
        outState.putString(EXTRA_STATE_RE_TIMES, rewatchingTimesInput.getTrimmedText())
        outState.putString(EXTRA_STATE_EPISODES, myEpisodesField.getTrimmedText())
        outState.putString(EXTRA_STATE_SUB_EPISODES, mySubEpisodesField.getTrimmedText())
        super.onSaveInstanceState(outState)
    }

    companion object {

        const val EXTRA_UPDATE_PARAMS = "extra.update_params"

        private const val EXTRA_RECORD_ID = "extra.record_id"
        private const val EXTRA_TITLE_TYPE = "extra.title_type"

        private const val EXTRA_STATE_START_DATE = "extra.state.start_date"
        private const val EXTRA_STATE_END_DATE = "extra.state.end_date"
        private const val EXTRA_STATE_IS_RE = "extra.state.is_re"
        private const val EXTRA_STATE_RE_TIMES = "extra.state.re_times"
        private const val EXTRA_STATE_EPISODES = "extra.state.episodes"
        private const val EXTRA_STATE_SUB_EPISODES = "extra.state.sub_episodes"

        fun startActivityForResult(context: Activity, recordId: Int, titleType: TitleType, requestCode: Int) {
            val intent = Intent(context, EditDetailsActivity::class.java)
                    .putExtra(EXTRA_RECORD_ID, recordId)
                    .putExtra(EXTRA_TITLE_TYPE, titleType.type)
            context.startActivityForResult(intent, requestCode)
        }
    }
}
