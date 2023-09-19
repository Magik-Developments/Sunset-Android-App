package com.madteam.sunset.ui.screens.discover

import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.model.SpotClusterItem
import com.madteam.sunset.repositories.DatabaseRepository
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

    private val _clusterInfo: MutableStateFlow<SpotClusterItem> =
        MutableStateFlow(SpotClusterItem())
    val clusterInfo: StateFlow<SpotClusterItem> = _clusterInfo

    private val _mapState: MutableStateFlow<MapState> = MutableStateFlow(MapState())
    val mapState: StateFlow<MapState> = _mapState

    private val _originalMapState: MutableStateFlow<MapState> = MutableStateFlow(MapState())
    val originalMapState: StateFlow<MapState> = _originalMapState

    private val _userLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val userLocation: StateFlow<LatLng> = _userLocation

    private val _goToUserLocation: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val goToUserLocation: StateFlow<Boolean> = _goToUserLocation

    private val _scoreFilter: MutableStateFlow<Int> = MutableStateFlow(0)
    val scoreFilter: StateFlow<Int> = _scoreFilter

    private val _locationFilter: MutableStateFlow<List<SpotAttribute>> = MutableStateFlow(listOf())
    val locationFilter: StateFlow<List<SpotAttribute>> = _locationFilter

    init {
        viewModelScope.launch {
            databaseRepository.getSpotsLocations().collect { spots ->
                _mapState.value = _mapState.value.copy(clusterItems = spots)
                _originalMapState.value = _mapState.value.copy(clusterItems = spots)
            }
        }
    }

    fun clusterVisibility(clusterItem: SpotClusterItem) {
        _clusterInfo.value = clusterItem
    }

    fun updateUserLocation(location: LatLng) {
        val loc = Location(LocationManager.GPS_PROVIDER)
        loc.latitude = location.latitude
        loc.longitude = location.longitude
        _userLocation.value = location
        _mapState.value = _mapState.value.copy(lastKnownLocation = loc)
    }

    fun setGoToUserLocation(state: Boolean) {
        _goToUserLocation.value = state
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
        _mapState.value = _mapState.value.copy(clusterItems = mapStateFiltered)
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

    private fun removeFilters() {
        _mapState.value = _originalMapState.value
    }

}