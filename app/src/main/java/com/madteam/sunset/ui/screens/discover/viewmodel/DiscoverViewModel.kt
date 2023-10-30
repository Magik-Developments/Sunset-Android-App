package com.madteam.sunset.ui.screens.discover.viewmodel

import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.model.SpotClusterItem
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.ui.screens.discover.state.DiscoverUIEvent
import com.madteam.sunset.ui.screens.discover.state.DiscoverUIState
import com.madteam.sunset.utils.googlemaps.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _originalMapState: MutableStateFlow<MapState> = MutableStateFlow(MapState())

    private val _state: MutableStateFlow<DiscoverUIState> = MutableStateFlow(DiscoverUIState())
    val state: StateFlow<DiscoverUIState> = _state

    init {
        getSpotsLocations()
    }

    fun onEvent(event: DiscoverUIEvent) {
        when (event) {
            is DiscoverUIEvent.ClusterVisibility -> {
                clusterVisibility(event.clusterItem)
            }

            is DiscoverUIEvent.GoToUserLocation -> {
                setGoToUserLocation(event.enabled)
            }

            is DiscoverUIEvent.UpdateUserLocation -> {
                updateUserLocation(event.location)
            }
        }
    }

    private fun getSpotsLocations() {
        viewModelScope.launch {
            databaseRepository.getSpotsLocations().collect { spots ->
                _state.value =
                    _state.value.copy(mapState = _state.value.mapState.copy(clusterItems = spots))
                _originalMapState.value = _originalMapState.value.copy(clusterItems = spots)
            }
        }
    }

    private fun clusterVisibility(clusterItem: SpotClusterItem) {
        _state.value = _state.value.copy(clusterInfo = clusterItem)
    }

    private fun updateUserLocation(location: LatLng) {
        val loc = Location(LocationManager.GPS_PROVIDER)
        loc.latitude = location.latitude
        loc.longitude = location.longitude
        _state.value = _state.value.copy(userLocation = location)
        _state.value =
            _state.value.copy(mapState = _state.value.mapState.copy(lastKnownLocation = loc))
    }

    private fun setGoToUserLocation(state: Boolean) {
        _state.value = _state.value.copy(goToUserLocation = state)
    }

    fun applyFilters(
        scoreFilter: Int,
        locationFilter: List<SpotAttribute>
    ) {
        var mapStateFiltered = _originalMapState.value.clusterItems
        mapStateFiltered = applyScoreFilter(scoreFilter, mapStateFiltered)
        if (locationFilter.isNotEmpty()) {
            mapStateFiltered = applyLocationFilter(locationFilter, mapStateFiltered)
        }
        _state.value =
            _state.value.copy(mapState = _state.value.mapState.copy(clusterItems = mapStateFiltered))
    }

    private fun applyScoreFilter(
        scoreFilter: Int,
        clusterItems: List<SpotClusterItem>
    ): List<SpotClusterItem> {
        return clusterItems.filter { it.spot.score > scoreFilter }
    }

    private fun applyLocationFilter(
        locationFilter: List<SpotAttribute>,
        clusterItems: List<SpotClusterItem>
    ): List<SpotClusterItem> {
        return clusterItems.filter { clusterItem ->
            locationFilter.any { filterAttribute ->
                clusterItem.spot.attributes.contains(filterAttribute)
            }
        }
    }

}