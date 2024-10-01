package com.g.pocketmal.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.g.pocketmal.domain.InListStatus
import com.g.pocketmal.domain.ListCounts
import com.g.pocketmal.domain.TitleType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusSelectorBottomSheet(
    titleType: TitleType,
    selectedStatus: InListStatus,
    counts: ListCounts,
    onNewStatusSelected: (InListStatus) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val statusSelectorItems = listOf(
        StatusSelectorItem(
            label = if (titleType.isAnime()) "Watching" else "Reading",
            status = InListStatus.IN_PROGRESS,
            count = counts.inProgressCount.toString(),
            isSelected = selectedStatus == InListStatus.IN_PROGRESS,
        ),
        StatusSelectorItem(
            label = "Completed",
            count = counts.completedCount.toString(),
            status = InListStatus.COMPLETED,
            isSelected = selectedStatus == InListStatus.COMPLETED,
        ),
        StatusSelectorItem(
            label = "On hold",
            count = counts.onHoldCount.toString(),
            status = InListStatus.ON_HOLD,
            isSelected = selectedStatus == InListStatus.ON_HOLD,
        ),
        StatusSelectorItem(
            label = "Dropped",
            count = counts.droppedCount.toString(),
            status = InListStatus.DROPPED,
            isSelected = selectedStatus == InListStatus.DROPPED,
        ),
        StatusSelectorItem(
            label = if (titleType.isAnime()) "Plan to watch" else "Plan to read",
            count = counts.plannedCount.toString(),
            status = InListStatus.PLANNED,
            isSelected = selectedStatus == InListStatus.PLANNED,
        ),
    )
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp, top = 24.dp, start = 36.dp, end = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val titleTypeLabel = if (titleType.isAnime()) "Anime" else "Manga"
            Text(
                text = "Your $titleTypeLabel List",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(36.dp))
            statusSelectorItems.forEach { item ->
                if (item.isSelected) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {},
                    ) {
                        StatusButtonContent(item.label, item.count)
                    }
                } else {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onNewStatusSelected(item.status)
                        },
                    ) {
                        StatusButtonContent(item.label, item.count)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            StatusButtonContent(
                label = "Total",
                value = counts.generalCount.toString(),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatusButtonContent(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(modifier = Modifier.width(54.dp))
        Text(text = label)
        Text(
            text = value,
            modifier = Modifier.width(54.dp),
            textAlign = TextAlign.End,
        )
    }
}

data class StatusSelectorItem(
    val label: String,
    val count: String,
    val status: InListStatus,
    val isSelected: Boolean,
)
