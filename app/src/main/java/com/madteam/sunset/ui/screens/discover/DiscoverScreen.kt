package com.madteam.sunset.ui.screens.discover

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.madteam.sunset.model.SpotClusterItem
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.AddSpotFAB
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.theme.SunsetTheme
import com.madteam.sunset.utils.getCurrentLocation
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.clusters.CustomClusterRenderer
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterManager
import com.madteam.sunset.utils.googlemaps.setMapProperties
import com.madteam.sunset.utils.googlemaps.updateCameraLocation
import com.madteam.sunset.utils.hasLocationPermission

const val MAP_PADDING = 200

@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: DiscoverViewModel = hiltViewModel()
) {

    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
    val clusterInfo by viewModel.clusterInfo.collectAsStateWithLifecycle()
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()
    val goToUserLocation by viewModel.goToUserLocation.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { SunsetBottomNavigation(navController) },
        floatingActionButton = {
            if (!clusterInfo.isSelected) {
                AddSpotFAB {
                    navController.navigate(SunsetRoutes.AddSpotScreen.route)
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                DiscoverContent(
                    mapState = mapState,
                    selectedCluster = { clusterItem ->
                        viewModel.clusterVisibility(clusterItem.copy(isSelected = true))
                    },
                    userLocation = userLocation,
                    updateUserLocation = viewModel::updateUserLocation,
                    goToUserLocation = goToUserLocation,
                    setGoToUserLocation = viewModel::setGoToUserLocation
                )
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp),
                    visible = clusterInfo.isSelected,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it + 200 })
                ) {
                    SpotClusterInfo(
                        clusterInfo,
                        onClose = { clusterItem ->
                            viewModel.clusterVisibility(clusterItem.copy(isSelected = false))
                        },
                        onItemClicked = { navController.navigate("spot_detail_screen/spotReference=${clusterInfo.spot.id}") })
                }
            }
        }
    )
}

@Composable
fun DiscoverContent(
    mapState: MapState,
    selectedCluster: (SpotClusterItem) -> Unit,
    userLocation: LatLng,
    updateUserLocation: (LatLng) -> Unit,
    goToUserLocation: Boolean,
    setGoToUserLocation: (Boolean) -> Unit
) {

    val cameraPositionState = rememberCameraPositionState()
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

    LaunchedEffect(key1 = goToUserLocation) {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = setMapProperties(mapState),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        )
    ) {
        SetupClusterManagerAndRenderers(
            mapState = mapState,
            selectedCluster = selectedCluster,
            cameraPositionState = cameraPositionState,
            userLocation = userLocation,
            goToUserLocation = goToUserLocation,
            setGoToUserLocation = setGoToUserLocation
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFFFB600)),
            onClick = {
                if (hasLocationPermission(context)) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                        setGoToUserLocation(true)
                    }
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDiscoverContent() {
    SunsetTheme {

    }
}

@SuppressLint("PotentialBehaviorOverride")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun SetupClusterManagerAndRenderers(
    mapState: MapState,
    selectedCluster: (SpotClusterItem) -> Unit,
    cameraPositionState: CameraPositionState,
    userLocation: LatLng,
    goToUserLocation: Boolean,
    setGoToUserLocation: (Boolean) -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    MapEffect(key1 = mapState.clusterItems, key2 = userLocation, key3 = goToUserLocation) { map ->
        if (mapState.clusterItems.isNotEmpty()) {
            val clusterManager = ZoneClusterManager(context, map)
            val clusterRenderer = CustomClusterRenderer(context, map, clusterManager)
            clusterManager.addItems(mapState.clusterItems)

            clusterManager.setOnClusterClickedListener { clusterItem ->
                selectedCluster(clusterItem)
            }

            clusterManager.renderer = clusterRenderer
            map.setOnCameraIdleListener(clusterManager)
            map.setOnMarkerClickListener(clusterManager)
            clusterManager.setOnClusterClickListener { cluster ->
                cluster.items.firstOrNull()?.let {
                    selectedCluster(it)
                }
                true
            }
        }
        if (userLocation.longitude != 0.0 && userLocation.latitude != 0.0 && goToUserLocation) {
            map.updateCameraLocation(scope, cameraPositionState, userLocation, 10f)
        }
        setGoToUserLocation(false)
    }
}