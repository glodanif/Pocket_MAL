package com.g.pocketmal.ui.editdetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import org.checkerframework.checker.units.qual.s
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
                        Log.i("EditDetailsActivity", "Params: $params")
                        //setResult(RESULT_OK, Intent().putExtra(EXTRA_UPDATE_PARAMS, params))
                        //finish()
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

internal val reValues = listOf("Not Set", "Very Low", "Low", "Medium", "High", "Very High")
internal val priorities = listOf("Low", "Medium", "High")

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
        onSaveClicked = {
            onSavePressed(viewModel.updateParameters)
        },
        onStartDateChanged = { startDate ->
            viewModel.updateStartDate(startDate)
        },
        onFinishDateChanged = { finishDate ->
            viewModel.updateFinishDate(finishDate)
        },
        onReChanged = { isRe ->
            viewModel.updateRe(isRe, titleType)
        },
        onCommentsChanged = { comments ->
            viewModel.updateComments(comments)
        },
        onReValueChanged = { reValue ->
            viewModel.updateReValue(reValue, titleType)
        },
        onPriorityChanged = { priority ->
            viewModel.updatePriority(priority)
        },
        onReTimesChanged = { reTimes ->
            viewModel.updateReTimes(reTimes, titleType)
        },
        onBackPressed = onBackPressed,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditDetailsScreen(
    recordDetailsState: EditDetailsState,
    onStartDateChanged: (Long?) -> Unit,
    onFinishDateChanged: (Long?) -> Unit,
    onReChanged: (Boolean) -> Unit,
    onCommentsChanged: (String) -> Unit,
    onReValueChanged: (Int) -> Unit,
    onPriorityChanged: (Int) -> Unit,
    onReTimesChanged: (Int?) -> Unit,
    onSaveClicked: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Details") },
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
                is EditDetailsState.RecordDetails ->

                    Column {
                        EditDetailsView(
                            modifier = Modifier.weight(1f),
                            record = recordDetailsState.details,
                            onStartDateChanged = onStartDateChanged,
                            onFinishDateChanged = onFinishDateChanged,
                            onReChanged = onReChanged,
                            onCommentsChanged = onCommentsChanged,
                            onReValueChanged = onReValueChanged,
                            onPriorityChanged = onPriorityChanged,
                            onReTimesChanged = onReTimesChanged,
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp)
                                .background(MaterialTheme.colorScheme.background)
                                .padding(16.dp),
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onSaveClicked
                            ) {
                                Text(text = "Save")
                            }
                        }
                    }

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
    onStartDateChanged: (Long?) -> Unit,
    onFinishDateChanged: (Long?) -> Unit,
    onReChanged: (Boolean) -> Unit,
    onCommentsChanged: (String) -> Unit,
    onReValueChanged: (Int) -> Unit,
    onReTimesChanged: (Int?) -> Unit,
    onPriorityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var openStartedDateDialog by remember { mutableStateOf(false) }
    var openFinishedDateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        DatesSection(
            record = record,
            onNewStartDateClicked = {
                openStartedDateDialog = true
            },
            onNewFinishDateClicked = {
                openFinishedDateDialog = true
            }
        )
        SectionsDivider()
        PrioritySection(
            record = record,
            onPriorityChanged = onPriorityChanged,
        )
        if (record.isReAvailable) {
            SectionsDivider()
            ReSection(
                record = record,
                onReChanged = onReChanged,
            )
        }
        SectionsDivider()
        ReTimesSection(
            record = record,
            onReTimesChanged = onReTimesChanged,
        )
        SectionsDivider()
        ReValueSection(
            record = record,
            onReValueChanged = onReValueChanged,
        )
        SectionsDivider()
        CommentsSection(
            record = record,
            onCommentsChanged = onCommentsChanged,
        )
        SectionsDivider()
    }

    if (openStartedDateDialog) {
        DatePickerModal(
            preselectedDate = record.startDate,
            onDismiss = {
                openStartedDateDialog = false
            },
            onDateSelected = { newDate ->
                onStartDateChanged(newDate)
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
                onFinishDateChanged(newDate)
            }
        )
    }
}

@Composable
private fun SectionsDivider(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(28.dp))
}

@Composable
private fun DatesSection(
    record: RecordExtraDetailsViewEntity,
    onNewStartDateClicked: () -> Unit,
    onNewFinishDateClicked: () -> Unit,
) {
    Text(
        text = if (record.titleType == TitleType.ANIME) "Watching period" else "Reading period",
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        DateView(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            title = "Start date",
            dateFormatted = record.startDateFormatted,
            onClicked = onNewStartDateClicked
        )
        DateView(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            title = "Finish date",
            dateFormatted = record.finishDateFormatted,
            onClicked = onNewFinishDateClicked
        )
    }
}

@Composable
private fun ReSection(
    record: RecordExtraDetailsViewEntity,
    onReChanged: (Boolean) -> Unit,
) {
    var isRe by remember { mutableStateOf(record.isRe) }

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = if (record.titleType == TitleType.ANIME) "Rewatching" else "Rereading",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Switch(
            checked = isRe,
            onCheckedChange = { checked ->
                isRe = checked
                onReChanged(checked)
            },
        )
    }
}

@Composable
private fun ReTimesSection(
    record: RecordExtraDetailsViewEntity,
    onReTimesChanged: (Int?) -> Unit,
) {
    val titleText = if (record.titleType == TitleType.ANIME) "Times Rewatched" else "Times Reread"
    Text(
        text = titleText,
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier.width(96.dp),
            value = if (record.reTimes == null) "" else record.reTimes.toString(),
            onValueChange = { newTimes ->
                val number = newTimes.toIntOrNull()
                if (number != null && number in 0..999) {
                    onReTimesChanged(number)
                } else if (newTimes.isEmpty()) {
                    onReTimesChanged(null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(
                    text = "0",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.outline,
                    )
                )
            }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Rounded.Info,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = "$titleText clarification icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "This number should NOT include the first time you completed this series",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ReValueSection(
    record: RecordExtraDetailsViewEntity,
    onReValueChanged: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val titleText = if (record.titleType == TitleType.ANIME) "Rewatch Value" else "Reread Value"
    Text(
        text = titleText,
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(12.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .clickable(
                    onClick = { expanded = true },
                )
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                reValues[record.reValue],
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Rounded.ArrowDropDown,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                contentDescription = "$titleText dropdown arrow",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(196.dp)
        ) {
            reValues.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        onReValueChanged(index)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PrioritySection(
    record: RecordExtraDetailsViewEntity,
    onPriorityChanged: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = "Priority",
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .clickable(
                    onClick = { expanded = true },
                )
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                priorities[record.priority],
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Rounded.ArrowDropDown,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                contentDescription = "Priority dropdown arrow",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(196.dp)
        ) {
            priorities.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        onPriorityChanged(index)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun CommentsSection(
    record: RecordExtraDetailsViewEntity,
    onCommentsChanged: (String) -> Unit,
) {
    Text(
        text = "Notes",
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(4.dp))
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = record.comments ?: "",
        onValueChange = onCommentsChanged,
        minLines = 3,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    )
}

@Composable
private fun DateView(
    title: String,
    dateFormatted: String?,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable {
                onClicked()
            }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            ),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = dateFormatted ?: "Not Set",
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            ),
        )
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
