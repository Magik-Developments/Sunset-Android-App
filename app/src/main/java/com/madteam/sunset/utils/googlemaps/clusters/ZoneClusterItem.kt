package com.madteam.sunset.utils.googlemaps.clusters

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class ZoneClusterItem(
  val id: String,
  private val title: String,
  private val snippet: String,
  val markerPosition: LatLng
) : ClusterItem {

  override fun getSnippet() = snippet

  override fun getTitle() = title

  override fun getPosition() = markerPosition
}