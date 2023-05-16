package com.madteam.sunset.ui.screens.discover

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterItem
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterManager
import kotlinx.coroutines.launch

const val MAP_PADDING = 0

@Composable
fun DiscoverScreen(
  navController: NavController,
  viewModel: DiscoverViewModel = hiltViewModel()
) {

  val mapState = viewModel.mapState.value
  val selectedCluster = viewModel.selectedCluster.value

  Scaffold(
    bottomBar = { SunsetBottomNavigation(navController) },
    content = { paddingValues ->
      Box(
        modifier = Modifier.padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        DiscoverContent(
          mapState = mapState,
          setupClusterManager = viewModel::setupClusterManager,
          calculateZoneViewCenter = viewModel::calculateZoneLatLngBounds,
          onClusterClicked = { clusterItem ->
            viewModel.selectedCluster.value = clusterItem
          }
        )

        if (selectedCluster != null) {
          TestSelectedComposable(selectedCluster) { viewModel.selectedCluster.value = null }
        }

      }
    }
  )
}

@Composable fun TestSelectedComposable(selectedCluster: ZoneClusterItem, onClose: () -> Unit) {
  Card() {
    Text(text = selectedCluster.title)
    Text(text = selectedCluster.snippet)
    Button(onClick = onClose) {
      Text(text = "Close")
    }
  }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun DiscoverContent(
  mapState: MapState,
  setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
  calculateZoneViewCenter: () -> LatLngBounds,
  onClusterClicked: (ZoneClusterItem) -> Unit
) {

  val context = LocalContext.current
  val mapProperties = MapProperties(isMyLocationEnabled = mapState.lastKnownLocation != null)
  val cameraPositionState = rememberCameraPositionState()

  GoogleMap(
    modifier = Modifier.fillMaxSize(),
    properties = mapProperties,
    cameraPositionState = cameraPositionState,
    uiSettings = MapUiSettings()
  ) {

    val scope = rememberCoroutineScope()
    MapEffect(mapState.clusterItems) { map ->
      if (mapState.clusterItems.isNotEmpty()) {
        val clusterManager = setupClusterManager(context, map)
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterClickListener { cluster ->
          val clusterItem = cluster.items.firstOrNull()
          clusterItem?.let { onClusterClicked(it) }
          true
        }
        map.setOnMapLoadedCallback {
          if (mapState.clusterItems.isNotEmpty()) {
            scope.launch {
              cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngBounds(
                  calculateZoneViewCenter(),
                  MAP_PADDING
                )
              )
            }
          }
        }
      }
    }
  }
}
