package com.g.pocketmal.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.g.pocketmal.ui.externallinks.ExternalLinksActivity
import com.g.pocketmal.ui.sharingpatterns.SharingPatternsActivity
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.settings.presentation.SettingsViewModel
import com.g.pocketmal.ui.theme.PocketMalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : SkeletonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                SettingsContent(
                    onSetupExternalLinksClicked = {
                        ExternalLinksActivity.start(this)
                    },
                    onSetupSharingPatternsClicked = {
                        SharingPatternsActivity.start(this)
                    },
                    onBackPressed = {
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    viewModel: SettingsViewModel = hiltViewModel(),
    onSetupExternalLinksClicked: () -> Unit,
    onSetupSharingPatternsClicked: () -> Unit,
    onBackPressed: () -> Unit,
) {
    var isThemePickerDialogDisplayed by remember { mutableStateOf(false) }
    var isFloatingSharingButtonDialogDisplayed by remember { mutableStateOf(false) }
    var isDefaultListPickerDialogDisplayed by remember { mutableStateOf(false) }
    val settingsState by viewModel.settingsState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPaddings ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPaddings)
                .consumeWindowInsets(innerPaddings)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                )
                .fillMaxSize()
                .padding(vertical = 24.dp, horizontal = 16.dp),
        ) {
            Text(
                text = "List",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                        isDefaultListPickerDialogDisplayed = true
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Default list",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = settingsState.defaultList.toString(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                    )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Sync list on startup",
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    checked = settingsState.listAutoSync,
                    onCheckedChange = { checked ->
                        viewModel.setNewAutoSyncValue(checked)
                    },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Save sorting order",
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    checked = settingsState.saveSortingOrder,
                    onCheckedChange = { checked ->
                        viewModel.setNewSaveSortingOrderValue(checked)
                    },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Show tags in list",
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    checked = settingsState.showTagsInList,
                    onCheckedChange = { checked ->
                        viewModel.setNewShowTagsInListValue(checked)
                    },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Hide posters in list",
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    checked = settingsState.hidePostersInList,
                    onCheckedChange = { checked ->
                        viewModel.setNewHidePostersInListValue(checked)
                    },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                        isThemePickerDialogDisplayed = true
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Theme",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = settingsState.themeMode.toString(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                    )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "English titles",
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    checked = settingsState.englishTitles,
                    onCheckedChange = { checked ->
                        viewModel.setNewShowEnglishTitlesValue(checked)
                    },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "External links",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                        onSetupExternalLinksClicked()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Setup pattern",
                    style = MaterialTheme.typography.labelLarge,
                )
                Icon(
                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Open External links pattern setup icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Use external browser",
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    checked = settingsState.useExternalBrowser,
                    onCheckedChange = { checked ->
                        viewModel.setNewUseExternalBrowserValue(checked)
                    },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Floating Sharing Button",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Show button on list changes",
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    checked = settingsState.enableFloatingSharingButton,
                    onCheckedChange = { checked ->
                        viewModel.setNewEnableFloatingSharingButtonValue(checked)
                    },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                        onSetupSharingPatternsClicked()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Setup sharing patterns",
                    style = MaterialTheme.typography.labelLarge,
                )
                Icon(
                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Open Sharing text patterns setup icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                        isFloatingSharingButtonDialogDisplayed = true
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Appearance conditions",
                    style = MaterialTheme.typography.labelLarge,
                )
                Icon(
                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Open Sharing button appearance conditions setup icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "NSFW content",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "In your list",
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    checked = settingsState.showNsfwContentInList,
                    onCheckedChange = { checked ->
                        viewModel.setNewNsfwInListValue(checked)
                    },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "While exploring new titles",
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    checked = settingsState.showNsfwContentInExplore,
                    onCheckedChange = { checked ->
                        viewModel.setNewNsfwInExploreValue(checked)
                    },
                )
            }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }

    if (isThemePickerDialogDisplayed) {
        ThemePickerDialog(
            currentTheme = settingsState.themeMode,
            onThemeSelected = { theme ->
                isThemePickerDialogDisplayed = false
                viewModel.setNewThemeValue(theme)
            },
            onDismissRequest = {
                isThemePickerDialogDisplayed = false
            }
        )
    }

    if (isDefaultListPickerDialogDisplayed) {
        DefaultListPickerDialog(
            currentList = settingsState.defaultList,
            onDefaultListSelected = { defaultList ->
                isDefaultListPickerDialogDisplayed = false
                viewModel.setNewDefaultListValue(defaultList)
            },
            onDismissRequest = {
                isDefaultListPickerDialogDisplayed = false
            },
        )
    }

    if (isFloatingSharingButtonDialogDisplayed) {
        FloatingSharingButtonOptionsDialog(
            currentOptions = settingsState.floatingSharingButtonOptions,
            onOptionsChanged = { options ->
                isFloatingSharingButtonDialogDisplayed = false
                viewModel.setNewFloatingSharingButtonOptionsValue(options)
            },
            onDismissRequest = {
                isFloatingSharingButtonDialogDisplayed = false
            },

            )
    }
}
