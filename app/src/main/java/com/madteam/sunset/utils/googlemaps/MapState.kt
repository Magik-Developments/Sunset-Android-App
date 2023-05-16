package com.madteam.sunset.utils.googlemaps

import android.location.Location
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterItem

data class MapState(
  val lastKnownLocation: Location?,
  val clusterItems: List<ZoneClusterItem>,
)
