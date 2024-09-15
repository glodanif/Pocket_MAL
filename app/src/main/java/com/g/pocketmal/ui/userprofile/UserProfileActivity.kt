package com.g.pocketmal.ui.userprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.g.pocketmal.R
import com.g.pocketmal.argument
import com.g.pocketmal.ui.common.LoadingView
import com.g.pocketmal.ui.common.YesNoDialog
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.theme.PocketMalTheme
import com.g.pocketmal.ui.userprofile.presentation.UserProfileState
import com.g.pocketmal.ui.userprofile.presentation.UserProfileViewEntity
import com.g.pocketmal.ui.userprofile.presentation.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileActivity : SkeletonActivity() {

    private val userId: Int by argument(EXTRA_USER_ID, 0)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                UserProfileContent(
                    userId = userId,
                    onLoggedOut = {
                        redirectToLoginScreen()
                    },
                    onBackPressed = { finish() }
                )
            }
        }
    }

    companion object {

        private const val EXTRA_USER_ID = "extra.user_id"

        fun start(context: Context, userId: Int) {
            val intent = Intent(context, UserProfileActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            }
            context.startActivity(intent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserProfileContent(
    userId: Int,
    viewModel: UserProfileViewModel = hiltViewModel(),
    onLoggedOut: () -> Unit,
    onBackPressed: () -> Unit,
) {

    val userProfileState by viewModel.userProfileState.collectAsState()
    var userName by remember { mutableStateOf("User") }
    var menuExpanded by remember { mutableStateOf(false) }
    val state = userProfileState

    var isLogoutConfirmationOpened by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUserProfileFromDb(userId)
    }
    LaunchedEffect(state) {
        if (state is UserProfileState.UserProfileLoaded) {
            userName = state.userProfile.name
        } else if (state is UserProfileState.LoggedOut) {
            onLoggedOut()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$userName's Profile") },
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
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            Icons.Rounded.MoreVert,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    stringResource(id = R.string.logout),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                isLogoutConfirmationOpened = true
                            }
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .consumeWindowInsets(innerPaddings)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                )
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (state is UserProfileState.Loading) {
                LoadingView()
            } else if (state is UserProfileState.UserProfileLoaded) {
                UserProfileView(state.userProfile)
            }
        }
    }

    if (isLogoutConfirmationOpened) {
        YesNoDialog(
            text = "Do you really want to log out?",
            onYesPressed = {
                isLogoutConfirmationOpened = false
                viewModel.logout()
            },
            onNoPressed = {
                isLogoutConfirmationOpened = false
            },
        )
    }
}

@Composable
private fun UserProfileView(
    userProfile: UserProfileViewEntity,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserProfileHeader(userProfile)
        Spacer(modifier = Modifier.height(8.dp))
        UserProfileDetails(userProfile)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Anime stats",
            style = MaterialTheme.typography.titleLarge
        )
        UserAnimeListDetails(userProfile)
    }
}

@Composable
private fun UserProfileHeader(
    userProfile: UserProfileViewEntity,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colorScheme.inversePrimary),
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = userProfile.avatar,
                    contentScale = ContentScale.Crop,
                    contentDescription = "User profile picture",
                )
            }
            if (userProfile.isTraditionalGender) {
                val icon = if (userProfile.isFemale)
                    Icons.Rounded.Female else Icons.Rounded.Male
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        icon,
                        modifier = Modifier.size(32.dp),
                        contentDescription = "${userProfile.genderLabel} gender icon",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}

@Composable
private fun UserProfileDetails(
    userProfile: UserProfileViewEntity,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            shadowElevation = 8.dp,
            tonalElevation = 8.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Rounded.LocationOn,
                        modifier = Modifier.size(18.dp),
                        contentDescription = "Location icon",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = userProfile.location,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.Cake,
                            modifier = Modifier.size(18.dp),
                            contentDescription = "Birthday icon",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = userProfile.birthday,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Text(
                        text = "Joined on ${userProfile.joinDate}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun UserAnimeListDetails(
    userProfile: UserProfileViewEntity,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp)),
            shadowElevation = 8.dp,
            tonalElevation = 8.dp,
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                StatLabel("Total entries", userProfile.animeCounts.generalCount.toString())
                Spacer(modifier = Modifier.width(12.dp))
                StatLabel("Days spent", userProfile.animeSpentDays)
                Spacer(modifier = Modifier.width(12.dp))
                StatLabel("Mean score", userProfile.animeMeanScore)
            }
        }
    }
    MultiProgressBar(userProfile.progresses)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 36.dp, start = 16.dp, end = 16.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp)),
            shadowElevation = 8.dp,
            tonalElevation = 8.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProgressLabel(
                    modifier = Modifier.weight(1f),
                    status = "Watching",
                    value = userProfile.animeCounts.inProgressCount.toString(),
                )
                ProgressLabel(
                    modifier = Modifier.weight(1f),
                    status = "Completed", value = userProfile.animeCounts.completedCount.toString(),
                )
                ProgressLabel(
                    modifier = Modifier.weight(1f),
                    status = "On hold", value = userProfile.animeCounts.onHoldCount.toString(),
                )
                ProgressLabel(
                    modifier = Modifier.weight(1f),
                    status = "Dropped", value = userProfile.animeCounts.droppedCount.toString(),
                )
                ProgressLabel(
                    modifier = Modifier.weight(1f),
                    status = "Planned",
                    value = userProfile.animeCounts.plannedCount.toString(),
                )
            }
        }
    }
}

@Composable
private fun StatLabel(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = name, style = MaterialTheme.typography.labelSmall)
        Text(text = value, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun ProgressLabel(
    status: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                .copy(color = MaterialTheme.colorScheme.primary),
        )
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall
                .copy(color = MaterialTheme.colorScheme.primary),
        )
    }
}
