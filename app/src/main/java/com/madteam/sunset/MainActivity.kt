package com.madteam.sunset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.madteam.sunset.common.navigation.SunsetNavigation
import com.madteam.sunset.common.navigation.SunsetRoutes.WelcomeScreen
import com.madteam.sunset.ui.theme.SunsetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SunsetTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    SunsetNavigation(WelcomeScreen.route)
                }
            }
        }
    }
}