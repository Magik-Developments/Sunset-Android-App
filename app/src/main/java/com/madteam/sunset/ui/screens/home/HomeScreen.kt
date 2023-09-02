package com.madteam.sunset.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.SunsetInfoModule

@Composable
fun HomeScreen(
    navController: NavController
) {

    Scaffold(
        bottomBar = { SunsetBottomNavigation(navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                HomeContent(
                    navigateTo = navController::navigate
                )
            }
        }
    )
}

@Composable
fun HomeContent(
    navigateTo: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        CustomSpacer(size = 24.dp)
        SunsetInfoModule(
            clickToExplore = { navigateTo(SunsetRoutes.DiscoverScreen.route) }
        )
    }
}

@Composable
@Preview
fun MyProfileScreenPreview() {
    HomeContent(
        navigateTo = {}
    )
}
