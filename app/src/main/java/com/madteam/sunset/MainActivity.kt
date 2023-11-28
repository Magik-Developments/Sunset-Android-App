package com.madteam.sunset

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.navigation.SunsetNavigation
import com.madteam.sunset.ui.screens.settings.notifications.ui.SUNSETS_TIME_CHANNEL_ID
import com.madteam.sunset.ui.screens.settings.notifications.ui.SUNSETS_TIME_CHANNEL_NAME
import com.madteam.sunset.ui.theme.SunsetTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val FLEXIBLE_UPDATE_REQUEST_CODE = 33
private const val IMMEDIATE_UPDATE_REQUEST_CODE = 69

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Box(modifier = Modifier.safeDrawingPadding()) {
                SunsetTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        val currentUser = authRepository.getCurrentUser()
                        val isAlreadyLoggedIn = (currentUser != null && currentUser.isEmailVerified)
                        SunsetNavigation(isAlreadyLoggedIn = isAlreadyLoggedIn)
                    }
                }
            }
        }

        //Init AdMob
        MobileAds.initialize(this) {}

        //Set remoteConfig defaults
        getRemoteConfig()

        //Init In-App Update
        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkIfUpdateIsAvailable()

        //Create notifications channels
        createNotificationsChannels()
    }

    private fun getRemoteConfig() {
        remoteConfig.setDefaultsAsync(
            mapOf(
                "forceVersion" to false,
                "minVersion" to BuildConfig.VERSION_NAME
            )
        )
        remoteConfig.fetchAndActivate()
    }

    private fun checkIfUpdateIsAvailable() {
        val isVersionForced = remoteConfig.getBoolean("forceVersion")
        val minVersion = remoteConfig.getString("minVersion")
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                && isVersionForced
                && BuildConfig.VERSION_NAME < minVersion
            ) {
                startAppUpdate(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    IMMEDIATE_UPDATE_REQUEST_CODE
                )
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                startAppUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE, FLEXIBLE_UPDATE_REQUEST_CODE)
            }
        }
    }

    private fun createNotificationsChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val channel = NotificationChannel(
                SUNSETS_TIME_CHANNEL_ID,
                getString(SUNSETS_TIME_CHANNEL_NAME),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = R.color.sunset
                enableLights(true)
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMMEDIATE_UPDATE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    finish()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startAppUpdate(updateInfo: AppUpdateInfo, updateType: Int, requestCode: Int) {
        appUpdateManager.startUpdateFlowForResult(
            updateInfo,
            updateType,
            this,
            requestCode
        )
    }

}