package com.madteam.sunset.utils.googlemaps

import android.location.Location
import com.google.android.gms.maps.model.LatLngBounds
import com.madteam.sunset.model.SpotClusterItem

data class MapState(
    val lastKnownLocation: Location?,
    val clusterItems: List<SpotClusterItem>,
) {
    constructor() : this(
        lastKnownLocation = null,
        clusterItems = emptyList()
    )

    fun getBounds() = LatLngBounds.builder().apply {
        clusterItems.map { clusterItem ->
            include((clusterItem.position))
        }
    }.build()
}
