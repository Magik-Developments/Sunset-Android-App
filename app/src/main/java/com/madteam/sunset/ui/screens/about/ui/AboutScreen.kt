package com.madteam.sunset.ui.screens.about.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.common.SunsetLogoImage

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
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            AboutContent()
        }
    }

}

@Composable
fun AboutContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SunsetLogoImage()

    }

}