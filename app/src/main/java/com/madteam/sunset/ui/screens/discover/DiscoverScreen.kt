package com.madteam.sunset.ui.screens.discover

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.madteam.sunset.model.SpotClusterItem
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.clusters.CustomClusterRenderer
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterManager
import com.madteam.sunset.utils.googlemaps.setMapProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val MAP_PADDING = 200

@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: DiscoverViewModel = hiltViewModel()
) {

    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
    val selectedCluster by viewModel.selectedCluster.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { SunsetBottomNavigation(navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                DiscoverContent(
                    mapState = mapState,
                    selectedCluster = { clusterItem ->
                        viewModel.setSelectedCluster(clusterItem)
                    }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp)
                ) {
                    AnimatedVisibility(
                        visible = selectedCluster != null, enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(durationMillis = 300)
                        )
                    ) {
                        if (selectedCluster != null) {
                            SpotClusterInfo(selectedCluster!!) {
                                viewModel.setSelectedCluster(null)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun DiscoverContent(
    mapState: MapState,
    selectedCluster: (SpotClusterItem) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = setMapProperties(mapState),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings()
    ) {
        SetupClusterManagerAndRenderers(
            mapState = mapState,
            selectedCluster = selectedCluster,
            cameraPositionState = cameraPositionState
        )
    }
}

@SuppressLint("PotentialBehaviorOverride")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun SetupClusterManagerAndRenderers(
    mapState: MapState,
    selectedCluster: (SpotClusterItem) -> Unit,
    cameraPositionState: CameraPositionState
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    MapEffect(mapState.clusterItems) { map ->
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
                val clusterItem = cluster.items.firstOrNull()
                clusterItem?.let { selectedCluster(it) }
                true
            }
        }
        map.setupMapInteractions(mapState, scope, cameraPositionState)
    }
}

private fun GoogleMap.setupMapInteractions(
    mapState: MapState,
    scope: CoroutineScope,
    cameraPositionState: CameraPositionState
) {
    setOnMapLoadedCallback {
        if (mapState.clusterItems.isNotEmpty()) {
            scope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngBounds(
                        mapState.getBounds(),
                        MAP_PADDING
                    )
                )
            }
        }
    }
}