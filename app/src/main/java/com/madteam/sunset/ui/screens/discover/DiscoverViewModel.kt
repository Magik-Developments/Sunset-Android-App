package com.madteam.sunset.ui.screens.discover

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.clusters.CustomClusterRenderer
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterItem
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor() : ViewModel() {

  val selectedCluster: MutableState<ZoneClusterItem?> = mutableStateOf(null)

  val mapState: MutableState<MapState> = mutableStateOf(
    MapState(
      lastKnownLocation = null,
      clusterItems = listOf(
        ZoneClusterItem(
          id = "zone-1",
          title = "Zone 1",
          snippet = "This is Zone 1.",
          markerPosition = LatLng(41.497, 2.045)
        ),
        ZoneClusterItem(
          id = "zone-2",
          title = "Zone 2",
          snippet = "This is Zone 2.",
          markerPosition = LatLng(41.462, 2.101)
        )
      )
    )
  )

  @SuppressLint("MissingPermission")
  fun getDeviceLocation(
    fusedLocationProviderClient: FusedLocationProviderClient
  ) {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    try {
      val locationResult = fusedLocationProviderClient.lastLocation
      locationResult.addOnCompleteListener { task ->
        if (task.isSuccessful) {
          mapState.value = mapState.value.copy(
            lastKnownLocation = task.result,
          )
        }
      }
    } catch (e: SecurityException) {
      // Show error or something
    }
  }

  fun setupClusterManager(
    context: Context,
    map: GoogleMap,
  ): ZoneClusterManager {
    val clusterManager = ZoneClusterManager(context, map)
    val clusterRenderer = CustomClusterRenderer(context, map, clusterManager)
    clusterManager.addItems(mapState.value.clusterItems)
    clusterManager.setOnClusterClickedListener { clusterItem ->
      selectedCluster.value = clusterItem
    }
    clusterManager.renderer = clusterRenderer
    return clusterManager
  }

  fun calculateZoneLatLngBounds(): LatLngBounds {
    val builder = LatLngBounds.builder()
    for (clusterItem in mapState.value.clusterItems) {
      builder.include(clusterItem.position)
    }
    return builder.build()
  }
}