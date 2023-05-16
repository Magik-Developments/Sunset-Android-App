package com.madteam.sunset.ui.screens.discover

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterManager
import kotlinx.coroutines.launch

@Composable
fun DiscoverScreen(
  navController: NavController,
  viewModel: DiscoverViewModel = hiltViewModel()
) {

  val mapState = viewModel.mapState.value

  Scaffold(
    bottomBar = { SunsetBottomNavigation(navController) },
    content = { paddingValues ->
      Box(
        modifier = Modifier.padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        DiscoverContent(
          mapState = mapState,
          viewModel::setupClusterManager,
          viewModel::calculateZoneLatLngBounds
        )
      }
    }
  )
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun DiscoverContent(
  mapState: MapState,
  setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
  calculateZoneViewCenter: () -> LatLngBounds
) {

  val context = LocalContext.current
  val mapProperties = MapProperties(isMyLocationEnabled = mapState.lastKnownLocation != null)
  val cameraPositionState = rememberCameraPositionState()

  GoogleMap(
    modifier = Modifier.fillMaxSize(),
    properties = mapProperties,
    cameraPositionState = cameraPositionState
  ) {

    val scope = rememberCoroutineScope()
    MapEffect(mapState.clusterItems) { map ->
      if (mapState.clusterItems.isNotEmpty()) {
        val clusterManager = setupClusterManager(context, map)
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
        mapState.clusterItems.forEach { clusterItem ->
          map.addPolygon(clusterItem.polygonOptions)
        }
        map.setOnMapLoadedCallback {
          if (mapState.clusterItems.isNotEmpty()) {
            scope.launch {
              cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngBounds(
                  calculateZoneViewCenter(),
                  0
                ),
              )
            }
          }
        }
      }
    }
  }
}
