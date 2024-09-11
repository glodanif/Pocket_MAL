package com.g.pocketmal.ui.seasonal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.g.pocketmal.data.util.PartOfYear
import com.g.pocketmal.ui.common.ToggleButton
import com.g.pocketmal.ui.common.yearpicker.YearPicker
import com.g.pocketmal.ui.common.yearpicker.rememberYearPickerState
import com.g.pocketmal.ui.seasonal.presentation.Season
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonSelectorBottomSheet(
    selectedSeason: Season,
    onDismissRequest: () -> Unit,
    onApplyClicked: (Season) -> Unit,
    modifier: Modifier = Modifier,
) {

    val currentYear = remember {
        Calendar.getInstance().apply {
            time = Date()
        }.get(Calendar.YEAR)
    }

    val years = remember { (1950..currentYear).map { it.toString() } }
    val yearsPickerState = rememberYearPickerState()

    val allSeasons = PartOfYear.entries
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var partOfYear by remember { mutableStateOf(selectedSeason.partOfYear) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,

        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    allSeasons.subList(0, 2).forEach { season ->
                        ToggleButton(
                            label = season.toString(),
                            isSelected = partOfYear == season,
                            onClick = { partOfYear = season },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    allSeasons.subList(2, 4).forEach { season ->
                        ToggleButton(
                            label = season.toString(),
                            isSelected = partOfYear == season,
                            onClick = { partOfYear = season },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            YearPicker(
                state = yearsPickerState,
                items = years,
                selectedYear = selectedSeason.year,
                visibleItemsCount = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onDismissRequest()
                    val newSeason = Season(
                        year = yearsPickerState.selectedItem.toInt(),
                        partOfYear = partOfYear,
                    )
                    onApplyClicked(newSeason)
                },
            ) {
                Text(text = "Apply")
            }
        }
    }
}
