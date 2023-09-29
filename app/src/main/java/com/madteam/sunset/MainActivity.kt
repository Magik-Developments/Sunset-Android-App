package com.madteam.sunset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.MobileAds
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.navigation.SunsetNavigation
import com.madteam.sunset.ui.theme.SunsetTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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

        MobileAds.initialize(this) {}
    }
}