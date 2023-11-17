package com.madteam.sunset.ui.screens.discover.state

import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.model.SpotClusterItem
import com.madteam.sunset.utils.googlemaps.MapState

data class DiscoverUIState(
    val clusterInfo: SpotClusterItem = SpotClusterItem(),
    val mapState: MapState = MapState(),
    val userLocation: LatLng = LatLng(0.0, 0.0),
    val goToUserLocation: Boolean = false,
    val scoreFilter: Int = 0,
    val locationFilter: List<SpotAttribute> = listOf()
)