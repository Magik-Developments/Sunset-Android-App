package com.madteam.sunset.ui.screens.about.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.GoBackTopAppBar

@Composable
fun AboutScreen(
    navController: NavController,
) {

    Scaffold(
        topBar = {
            GoBackTopAppBar(title = R.string.about_us) {
                navController.popBackStack()
            }
        }
    ) {

    }

}

@Composable
fun AboutContent() {

}