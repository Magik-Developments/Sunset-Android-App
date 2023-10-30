package com.madteam.sunset.ui.screens.selectLocation.viewmodel

import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.ui.screens.selectLocation.state.SelectLocationUIEvent
import com.madteam.sunset.ui.screens.selectLocation.state.SelectLocationUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SelectLocationViewModel @Inject constructor(
) : ViewModel() {

    private val _state: MutableStateFlow<SelectLocationUIState> =
        MutableStateFlow(SelectLocationUIState())
    val state: StateFlow<SelectLocationUIState> = _state

    fun onEvent(event: SelectLocationUIEvent) {
        when (event) {
            is SelectLocationUIEvent.SetGoToUserLocation -> {
                setGoToUserLocation(event.state)
            }

            is SelectLocationUIEvent.UpdateSelectedLocation -> {
                updateSelectedLocation(event.location)
            }

            is SelectLocationUIEvent.UpdateUserLocation -> {
                updateUserLocation(event.location)
            }
        }
    }

    private fun updateSelectedLocation(location: LatLng) {
        _state.value = _state.value.copy(selectedLocation = location)
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

}