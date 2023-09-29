package com.madteam.sunset.utils.googlemaps.clusters

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager
import com.madteam.sunset.data.model.SpotClusterItem

class ZoneClusterManager(
    context: Context,
    googleMap: GoogleMap,
) : ClusterManager<SpotClusterItem>(context, googleMap, MarkerManager(googleMap)) {
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

    private var onClusterClicked: (SpotClusterItem) -> Unit = {}

    fun setOnClusterClickedListener(listener: (SpotClusterItem) -> Unit) {
        onClusterClicked = listener
    }
}
