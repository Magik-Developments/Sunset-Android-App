package com.madteam.sunset.utils.googlemaps.clusters

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class CustomClusterRenderer(
  context: Context,
  map: GoogleMap,
  clusterManager: ZoneClusterManager
) :
  DefaultClusterRenderer<SpotClusterItem>(context, map, clusterManager) {

  override fun onBeforeClusterItemRendered(item: SpotClusterItem, markerOptions: MarkerOptions) {
    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
  }

}
