package com.g.pocketmal.ui.common.yearpicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberYearPickerState() = remember { YearPickerState() }

class YearPickerState {
    var selectedItem by mutableStateOf("")
}
