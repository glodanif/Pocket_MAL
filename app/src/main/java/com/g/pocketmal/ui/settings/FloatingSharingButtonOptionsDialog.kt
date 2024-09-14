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
import androidx.compose.material.icons.rounded.Share
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
import com.g.pocketmal.domain.FloatingSharingButtonOptions
import com.g.pocketmal.domain.ThemeMode

@Composable
fun FloatingSharingButtonOptionsDialog(
    currentOptions: FloatingSharingButtonOptions,
    onDismissRequest: () -> Unit,
    onOptionsChanged: (FloatingSharingButtonOptions) -> Unit
) {
    var selectedOptions by remember { mutableStateOf(currentOptions) }

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
                modifier = Modifier
                    .padding(24.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Rounded.Share,
                    contentDescription = "Floating sharing button options dialog",
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Floating Sharing Button",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOptions =
                                selectedOptions.copy(onScoreChanges = !selectedOptions.onScoreChanges)
                        }
                        .padding(vertical = 8.dp),
                ) {
                    androidx.compose.material3.Checkbox(
                        checked = selectedOptions.onScoreChanges,
                        onCheckedChange = {
                            selectedOptions = selectedOptions.copy(onScoreChanges = it)
                        }
                    )
                    Text(
                        text = "On Score Changes",
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOptions =
                                selectedOptions.copy(onStatusChanges = !selectedOptions.onStatusChanges)
                        }
                        .padding(vertical = 8.dp),
                ) {
                    androidx.compose.material3.Checkbox(
                        checked = selectedOptions.onStatusChanges,
                        onCheckedChange = {
                            selectedOptions = selectedOptions.copy(onStatusChanges = it)
                        }
                    )
                    Text(
                        text = "On Status Changes",
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOptions =
                                selectedOptions.copy(onProgressChanges = !selectedOptions.onProgressChanges)
                        }
                        .padding(vertical = 8.dp),
                ) {
                    androidx.compose.material3.Checkbox(
                        checked = selectedOptions.onProgressChanges,
                        onCheckedChange = {
                            selectedOptions = selectedOptions.copy(onProgressChanges = it)
                        }
                    )
                    Text(
                        text = "On Progress Changes",
                        modifier = Modifier.padding(start = 8.dp),
                    )
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
                        onClick = { onOptionsChanged(selectedOptions) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}
