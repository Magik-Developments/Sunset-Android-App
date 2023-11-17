package com.madteam.sunset.ui.screens.selectLocation.state

import com.google.android.gms.maps.model.LatLng

sealed class SelectLocationUIEvent {
    data class UpdateSelectedLocation(val location: LatLng) : SelectLocationUIEvent()
    data class UpdateUserLocation(val location: LatLng) : SelectLocationUIEvent()
    data class SetGoToUserLocation(val state: Boolean) : SelectLocationUIEvent()
}