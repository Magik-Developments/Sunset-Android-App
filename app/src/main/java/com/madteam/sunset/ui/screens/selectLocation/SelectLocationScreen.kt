package com.madteam.sunset.ui.screens.selectLocation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.setMapProperties

@Composable
fun SelectLocationScreen(
    navController: NavController,
    viewModel: SelectLocationViewModel = hiltViewModel()
) {

    val mapState by viewModel.mapState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GoForwardTopAppBar(
                title = R.string.add_location,
                onQuitClick = { navController.popBackStack() },
                onContinueClick = { /*TODO*/ },
                canContinue = false
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SelectLocationContent(
                    mapState = mapState
                )
            }
        }
    )
}

@Composable
fun SelectLocationContent(
    mapState: MapState
) {

    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = setMapProperties(mapState = mapState),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        )
    ) {

    }

}