package com.g.pocketmal.ui.externallinks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.ui.PocketMalTheme
import com.g.pocketmal.ui.SkeletonActivity
import com.g.pocketmal.ui.ThemeMode
import dagger.hilt.android.AndroidEntryPoint
import org.koin.android.ext.android.inject

@AndroidEntryPoint
class ExternalLinksActivity : SkeletonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExternalLinksContent(onBackPressed = { finish() })
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ExternalLinksActivity::class.java))
        }
    }
}

@Composable
fun ExternalLinksContent(
    onBackPressed: () -> Unit = {},
    viewModel: ExternalLinksViewModel = hiltViewModel(),
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val currentPattern by viewModel.pattern.collectAsState()

    PocketMalTheme(theme = themeMode) {
        ExternalLinksScreen(
            currentPattern = currentPattern,
            onBackPressed = onBackPressed,
            onPatternSaveClick = { pattern ->
                viewModel.saveNewPattern(pattern)
                onBackPressed()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExternalLinksScreen(
    currentPattern: String,
    onPatternSaveClick: (String) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    var enteredPattern by remember { mutableStateOf(currentPattern) }

    LaunchedEffect(currentPattern) {
        enteredPattern = currentPattern
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("External Links setup") },
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
        Column(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "In order to open external links you need to create your link pattern manually")
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Use {type} to set a title type (anime or manga), and {id} - title id on MAL")
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = enteredPattern,
                placeholder = { Text(text = "website.xyz/{type}?id={id}") },
                onValueChange = { newValue ->
                    enteredPattern = newValue
                },
                prefix = { Text(text = "https://") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(visible = enteredPattern.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(color = MaterialTheme.colorScheme.inversePrimary)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Your links will look like this",
                        style = MaterialTheme.typography.bodySmall
                    )
                    val linkBody = enteredPattern
                        .replace("{type}", "anime")
                        .replace("{id}", "73510")
                    Text(text = "https://$linkBody", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    enteredPattern = "myanimelist.net/{type}/{id}"
                }) {
                Text(text = "Use MAL format")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onPatternSaveClick(enteredPattern)
                }) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun PrefilledTextField(text: String, onTextChange: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text("Enter text") },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Preview
@Composable
fun PreviewLight() {
    PocketMalTheme(theme = ThemeMode.LIGHT) {
        ExternalLinksScreen(currentPattern = "")
    }
}

@Preview
@Composable
fun PreviewDark() {
    PocketMalTheme(theme = ThemeMode.DARK) {
        ExternalLinksScreen(currentPattern = "")
    }
}

@Preview
@Composable
fun PreviewBlack() {
    PocketMalTheme(theme = ThemeMode.BLACK) {
        ExternalLinksScreen(currentPattern = "")
    }
}
