package com.madteam.sunset.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.clustering.ClusterItem

data class SpotClusterItem(
    val id: String,
    val name: String,
    val spot: String,
    val location: GeoPoint,
    val isVisible: Boolean
) : ClusterItem {

    constructor() : this(
        id = "",
        name = "",
        spot = "",
        location = GeoPoint(0.0, 0.0),
        isVisible = false
    )


    override fun getTitle() = name
    override fun getSnippet(): String? = null
    override fun getPosition() = LatLng(location.latitude, location.longitude)
}