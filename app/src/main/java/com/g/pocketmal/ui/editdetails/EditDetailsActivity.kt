package com.g.pocketmal.ui.editdetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.g.pocketmal.argument
import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.transformedArgument
import com.g.pocketmal.ui.common.ErrorMessageView
import com.g.pocketmal.ui.common.LoadingView
import com.g.pocketmal.ui.editdetails.presentation.EditDetailsState
import com.g.pocketmal.ui.editdetails.presentation.EditDetailsViewModel
import com.g.pocketmal.ui.editdetails.presentation.RecordExtraDetailsViewEntity
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.theme.PocketMalTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class EditDetailsActivity : SkeletonActivity() {

    private val recordId: Int by argument(EXTRA_RECORD_ID, 0)
    private val titleType: TitleType by transformedArgument<Int, TitleType>(
        EXTRA_TITLE_TYPE,
        TitleType.ANIME
    ) { TitleType.from(it) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                EditDetailsContent(
                    recordId = recordId,
                    titleType = titleType,
                    onSavePressed = { params ->
                        setResult(RESULT_OK, Intent().putExtra(EXTRA_UPDATE_PARAMS, params))
                        finish()
                    },
                    onBackPressed = { finish() },
                )
            }
        }
    }

    companion object {

        const val EXTRA_UPDATE_PARAMS = "extra.update_params"

        private const val EXTRA_RECORD_ID = "extra.record_id"
        private const val EXTRA_TITLE_TYPE = "extra.title_type"

        fun startActivityForResult(
            context: Activity,
            recordId: Int,
            titleType: TitleType,
            requestCode: Int
        ) {
            val intent = Intent(context, EditDetailsActivity::class.java).apply {
                putExtra(EXTRA_RECORD_ID, recordId)
                putExtra(EXTRA_TITLE_TYPE, titleType.type)
            }
            context.startActivityForResult(intent, requestCode)
        }
    }
}

@Composable
private fun EditDetailsContent(
    recordId: Int,
    titleType: TitleType,
    viewModel: EditDetailsViewModel = hiltViewModel(),
    onSavePressed: (UpdateParams) -> Unit,
    onBackPressed: () -> Unit,
) {
    val recordDetailsState by viewModel.recordDetailsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecordDetails(recordId, titleType)
    }

    EditDetailsScreen(
        recordDetailsState = recordDetailsState,
        onSavePressed = onSavePressed,
        onBackPressed = onBackPressed,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditDetailsScreen(
    recordDetailsState: EditDetailsState,
    onSavePressed: (UpdateParams) -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recommendations") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
    ) { innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (recordDetailsState) {
                is EditDetailsState.RecordDetails -> EditDetailsView(
                    record = recordDetailsState.details,
                    onSaveClicked = {
                        onSavePressed(UpdateParams())
                    }
                )

                EditDetailsState.IncorrectInput ->
                    ErrorMessageView(message = "Unable to get the title id, please relaunch the app...")

                EditDetailsState.Loading -> LoadingView()

                EditDetailsState.NotFound ->
                    ErrorMessageView(message = "Unable to find the record in the database, please relaunch the app...")
            }
        }
    }
}

@Composable
private fun EditDetailsView(
    record: RecordExtraDetailsViewEntity,
    onSaveClicked: () -> Unit,
) {
    var openStartedDateDialog by remember { mutableStateOf(false) }
    var openFinishedDateDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DateView(
                modifier = Modifier.weight(1f),
                dateFormatted = record.startDateFormatted,
                onClicked = {
                    openStartedDateDialog = true
                }
            )
            DateView(
                modifier = Modifier.weight(1f),
                dateFormatted = record.finishDateFormatted,
                onClicked = {
                    openFinishedDateDialog = true
                }
            )
        }
        Button(onClick = { onSaveClicked() }) {
            Text(text = "Save")
        }
    }

    if (openStartedDateDialog) {
        DatePickerModal(
            preselectedDate = record.startDate,
            onDismiss = {
                openStartedDateDialog = false
            },
            onDateSelected = { newDate ->

            }
        )
    }

    if (openFinishedDateDialog) {
        DatePickerModal(
            preselectedDate = record.finishDate,
            onDismiss = {
                openFinishedDateDialog = false
            },
            onDateSelected = { newDate ->

            }
        )
    }
}

@Composable
private fun DateView(
    dateFormatted: String?,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable {
            onClicked()
        },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = dateFormatted ?: "Not Set")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    preselectedDate: Date?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = preselectedDate?.time
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("Set")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

/*rewatchingTimesInput.filters =
           arrayOf<InputFilter>(NumberInputFilter(NumberInputFilter.MAX_REWATCHING_NUMBER))

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
       // presenter.setRewatching(isChecked)
    }
}

override fun showEnteredValuesNotValid() {
    MessageDialog(this, getCurrentTheme(), getString(R.string.edit_details__invalid_quantity)).show()
}*/
