package com.g.pocketmal.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.g.pocketmal.ui.list.presentation.RecordListViewEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateRecordBottomSheet(
    record: RecordListViewEntity,
    onEpisodesClicked: () -> Unit,
    onSubEpisodesClicked: () -> Unit,
    onDismissRequest: () -> Unit,
) {

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
            Button(
                onClick = onEpisodesClicked,
                modifier = Modifier.height(72.dp).fillMaxWidth()
            ) {
                Text(text = "+1 ${record.episodesName}")
            }
            if (record.isSubEpisodesAvailable) {
                Spacer(modifier = Modifier.height(36.dp))
                Button(
                    onClick = onSubEpisodesClicked,
                    modifier = Modifier.height(72.dp).fillMaxWidth()
                ) {
                    Text(text = "+1 ${record.subEpisodesName}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
