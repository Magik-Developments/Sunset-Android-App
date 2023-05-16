package com.madteam.sunset.utils.googlemaps.clusters

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager

class ZoneClusterManager(
  context: Context,
  googleMap: GoogleMap,
): ClusterManager<ZoneClusterItem>(context, googleMap, MarkerManager(googleMap)) {
  init {
    setOnClusterClickListener { cluster ->
      val clusterItem = cluster.items.firstOrNull()
      clusterItem?.let { onClusterClicked(it) }
      true
    }

    setOnClusterItemClickListener { clusterItem ->
      onClusterClicked(clusterItem)
      true
    }
  }

  private var onClusterClicked: (ZoneClusterItem) -> Unit = {}

  fun setOnClusterClickedListener(listener: (ZoneClusterItem) -> Unit) {
    onClusterClicked = listener
  }

}
