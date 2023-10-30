package com.madteam.sunset.ui.screens.discover.state

import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SpotClusterItem

sealed class DiscoverUIEvent {
    data class UpdateUserLocation(val location: LatLng) : DiscoverUIEvent()
    data class GoToUserLocation(val enabled: Boolean) : DiscoverUIEvent()
    data class ClusterVisibility(val clusterItem: SpotClusterItem) : DiscoverUIEvent()
}