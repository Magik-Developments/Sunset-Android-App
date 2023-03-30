package com.madteam.sunset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madteam.sunset.ui.theme.SunsetTheme
import com.madteam.sunset.welcome.ui.BottomSheetSignUp
import com.madteam.sunset.welcome.ui.ModalBottomSheetLayout

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SunsetTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    NavHost(navController = navController, startDestination = "main") {
                        composable(route = "main") {
                            ModalBottomSheetLayout(navController)
                        }
                        composable(route = "second") {
                            BottomSheetSignUp()
                        }
                    }
                }
            }
        }
    }
}