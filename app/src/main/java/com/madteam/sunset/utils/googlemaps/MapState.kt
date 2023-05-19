package com.madteam.sunset.utils.googlemaps

import android.location.Location
import com.madteam.sunset.utils.googlemaps.clusters.SpotClusterItem

data class MapState(
  val lastKnownLocation: Location?,
  val clusterItems: List<SpotClusterItem>,
)
