package com.madteam.sunset.ui.screens.discover

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.madteam.sunset.repositories.DatabaseRepository
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.clusters.CustomClusterRenderer
import com.madteam.sunset.utils.googlemaps.clusters.SpotClusterItem
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
  private val databaseRepository: DatabaseRepository
) : ViewModel() {

  val selectedCluster: MutableState<SpotClusterItem?> = mutableStateOf(null)
  val mapState: MutableStateFlow<MapState> = MutableStateFlow(
    MapState(
      lastKnownLocation = null,
      clusterItems = emptyList()
    )
  )

  init {
    loadSpotsLocations()
  }

  private fun loadSpotsLocations() {
    viewModelScope.launch {
      databaseRepository.getSpotsLocations().collect { spots ->
        mapState.value = mapState.value.copy(clusterItems = spots)
      }
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