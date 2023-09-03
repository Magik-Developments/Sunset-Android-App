package com.madteam.sunset.ui.screens.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.model.SunsetTimeResponse
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.SunsetInfoModule
import com.madteam.sunset.utils.getCurrentLocation

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {

    val sunsetTimeInformation by viewModel.sunsetTimeInformation.collectAsStateWithLifecycle()
    val userLocality by viewModel.userLocality.collectAsStateWithLifecycle()
    val remainingTimeToSunset by viewModel.remainingTimeToSunset.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { SunsetBottomNavigation(navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                HomeContent(
                    sunsetTimeInformation = sunsetTimeInformation,
                    navigateTo = navController::navigate,
                    remainingTimeToSunset = remainingTimeToSunset,
                    updateUserLocation = viewModel::updateUserLocation,
                    userLocality = userLocality
                )
            }
        }
    )
}

@Composable
fun HomeContent(
    sunsetTimeInformation: SunsetTimeResponse,
    navigateTo: (String) -> Unit,
    remainingTimeToSunset: String,
    updateUserLocation: (LatLng) -> Unit,
    userLocality: String
) {

    val context = LocalContext.current

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                    }
                }
            }
        )

    LaunchedEffect(key1 = sunsetTimeInformation) {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        CustomSpacer(size = 24.dp)
        if (remainingTimeToSunset.isNotEmpty()) {
            SunsetInfoModule(
                sunsetTimeInformation = sunsetTimeInformation,
                userLocality = userLocality,
                remainingTimeToSunset = remainingTimeToSunset,
                clickToExplore = { navigateTo(SunsetRoutes.DiscoverScreen.route) }
            )
        }
    }
}

@Composable
@Preview
fun MyProfileScreenPreview() {

}
