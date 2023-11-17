package com.madteam.sunset.ui.screens.selectLocation.state

import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.utils.googlemaps.MapState

data class SelectLocationUIState(
    val mapState: MapState = MapState(),
    val selectedLocation: LatLng = LatLng(0.0, 0.0),
    val userLocation: LatLng = LatLng(0.0, 0.0),
    val goToUserLocation: Boolean = false
)