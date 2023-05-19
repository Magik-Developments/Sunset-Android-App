package com.madteam.sunset.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.madteam.sunset.repositories.DatabaseRepository
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.clusters.SpotClusterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
  private val databaseRepository: DatabaseRepository
) : ViewModel() {

  val selectedCluster: MutableStateFlow<SpotClusterItem?> = MutableStateFlow(null)
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

  fun calculateZoneLatLngBounds(): LatLngBounds {
    val builder = LatLngBounds.builder()
    for (clusterItem in mapState.value.clusterItems) {
      builder.include(clusterItem.position)
    }
    return builder.build()
  }
}