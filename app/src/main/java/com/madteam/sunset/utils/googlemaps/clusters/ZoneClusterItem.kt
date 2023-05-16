package com.madteam.sunset.utils.googlemaps.clusters

import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.clustering.ClusterItem
import com.madteam.sunset.utils.googlemaps.getCenterOfPolygon

data class ZoneClusterItem(
  val id: String,
  private val title: String,
  private val snippet: String,
  val polygonOptions: PolygonOptions
) : ClusterItem {

  override fun getSnippet() = snippet

  override fun getTitle() = title

  override fun getPosition() = polygonOptions.points.getCenterOfPolygon().center
}