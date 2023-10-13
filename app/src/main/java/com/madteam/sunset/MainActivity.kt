package com.madteam.sunset

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
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
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.navigation.SunsetNavigation
import com.madteam.sunset.ui.theme.SunsetTheme
import com.squareup.seismic.ShakeDetector
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val shakeDetector = ShakeDetector {
            Toast.makeText(this, "ddd", Toast.LENGTH_SHORT).show()
        }
        shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME)

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
    }
}