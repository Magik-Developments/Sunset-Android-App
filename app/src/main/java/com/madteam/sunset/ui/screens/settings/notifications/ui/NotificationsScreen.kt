package com.madteam.sunset.ui.screens.settings.notifications.ui

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.common.NotificationsPermissionDialog
import com.madteam.sunset.ui.common.SunsetButton
import com.madteam.sunset.ui.screens.settings.notifications.state.NotificationsUIEvent
import com.madteam.sunset.ui.screens.settings.notifications.state.NotificationsUIState
import com.madteam.sunset.ui.screens.settings.notifications.viewmodel.NotificationsViewModel
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.utils.hasNotificationsPermission

const val SUNSETS_TIME_CHANNEL_ID = "com.madteam.sunset.sunsetsnotifications"
const val SUNSETS_TIME_CHANNEL_NAME = "Sunsets Time"

@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GoBackTopAppBar(title = R.string.notifications) {
                navController.popBackStack()
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                NotificationsContent(
                    state = state,
                    setShowNotificationsPermissionDialog = {
                        viewModel.onEvent(
                            NotificationsUIEvent.SetShowNotificationsPermissionDialog(
                                it
                            )
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun NotificationsContent(
    state: NotificationsUIState,
    setShowNotificationsPermissionDialog: (Boolean) -> Unit
) {

    val context = LocalContext.current

    var notificationsGranted by remember {
        mutableStateOf(true)
    }

    var isSunsetsTimeChannelEnabled by remember {
        mutableStateOf(false)
    }

    val requestLocationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    notificationsGranted = true
                }
            })

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationsGranted = hasNotificationsPermission(context)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            isSunsetsTimeChannelEnabled = notificationManager.getNotificationChannel(
                SUNSETS_TIME_CHANNEL_ID
            )?.importance != NotificationManager.IMPORTANCE_NONE
        }
    }

    if (state.showNotificationsPermissionDialog && Build.VERSION.SDK_INT >= 33) {
        NotificationsPermissionDialog {
            requestLocationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            setShowNotificationsPermissionDialog(false)
        }
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.sunsets),
                        style = primaryBoldHeadlineS
                    )
                    Text(
                        text = stringResource(id = R.string.sunsets_notifications),
                        style = secondaryRegularBodyM
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Switch(
                        checked = isSunsetsTimeChannelEnabled,
                        onCheckedChange = {
                            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, SUNSETS_TIME_CHANNEL_ID)
                            context.startActivity(intent)
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Color(0xFFFFB600),
                            disabledCheckedTrackColor = Color(0xFFFFE094)
                        ),
                        enabled = notificationsGranted
                    )
                }
            }
        }
        CustomSpacer(size = 24.dp)
        if (!notificationsGranted) {
            SunsetButton(
                text = R.string.enable_notifications,
                onClick = {
                    setShowNotificationsPermissionDialog(true)
                }
            )
        }
    }
}