package com.g.pocketmal.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.g.pocketmal.domain.ThemeType

@Composable
fun ThemePickerDialog(
    currentTheme: ThemeType,
    onDismissRequest: () -> Unit,
    onThemeSelected: (ThemeType) -> Unit
) {
    var selectedTheme by remember { mutableStateOf(currentTheme) }

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Rounded.LightMode,
                    contentDescription = "Theme dialog",
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Theme",
                    modifier = Modifier.padding(16.dp),
                )
                ThemeType.entries.forEach { themeMode ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedTheme = themeMode }
                            .padding(vertical = 16.dp),
                    ) {
                        RadioButton(
                            selected = (themeMode == selectedTheme),
                            onClick = null,
                        )
                        Text(
                            text = themeMode.name,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = { onThemeSelected(selectedTheme) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}
