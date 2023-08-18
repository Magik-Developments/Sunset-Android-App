package com.madteam.sunset.ui.screens.selectLocation

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.utils.googlemaps.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SelectLocationViewModel @Inject constructor(
) : ViewModel() {

    private val _mapState: MutableStateFlow<MapState> = MutableStateFlow(MapState())
    val mapState: StateFlow<MapState> = _mapState

    private val _selectedLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val selectedLocation: StateFlow<LatLng> = _selectedLocation

    private val _userLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val userLocation: StateFlow<LatLng> = _userLocation

    private val _goToUserLocation: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val goToUserLocation: StateFlow<Boolean> = _goToUserLocation

    fun updateSelectedLocation(location: LatLng) {
        _selectedLocation.value = location
    }

    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
    }

    fun setGoToUserLocation(state: Boolean) {
        _goToUserLocation.value = state
    }

}