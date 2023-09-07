package com.madteam.sunset.ui.screens.discover

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
        _userLocation.value = location
    }

    fun setGoToUserLocation(state: Boolean) {
        _goToUserLocation.value = state
    }

    fun applyFilters(
        scoreFilter: Int,
        locationFilter: List<SpotAttribute>
    ) {
        applyScoreFilter(scoreFilter)
    }

    private fun applyScoreFilter(scoreFilter: Int) {
        val mapStateFiltered =
            _originalMapState.value.clusterItems.filter { it.spot.score > scoreFilter }
        _mapState.value = _mapState.value.copy(clusterItems = mapStateFiltered)
    }

    private fun applyLocationFilter(locationFilter: List<SpotAttribute>) {
        if (locationFilter.isNotEmpty()) {
            val mapStateFiltered = _mapState.value.clusterItems.filter { clusterItem ->
                locationFilter.any { filterAttribute ->
                    clusterItem.spot.attributes.contains(filterAttribute)
                }
            }
            _mapState.value = _mapState.value.copy(clusterItems = mapStateFiltered)
        }
    }

    private fun removeFilters() {
        _mapState.value = _originalMapState.value
    }

}