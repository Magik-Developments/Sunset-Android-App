package com.madteam.sunset.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.clustering.ClusterItem

data class SpotClusterItem(
    val id: String,
    val name: String,
    val spot: DocumentReference,
    val locationInLatLng: GeoPoint,
    val isSelected: Boolean,
    val featuredImages: List<String>,
    val location: String,
    val score: Float
) : ClusterItem {

    constructor() : this(
        id = "",
        name = "",
        spot = FirebaseFirestore.getInstance().document(""),
        locationInLatLng = GeoPoint(0.0, 0.0),
        isSelected = false,
        featuredImages = listOf(),
        location = "",
        score = 0.0f
    )

    override fun getTitle() = name
    override fun getSnippet(): String? = null
    override fun getPosition() = LatLng(locationInLatLng.latitude, locationInLatLng.longitude)
}