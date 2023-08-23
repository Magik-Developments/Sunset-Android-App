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
    val location: GeoPoint,
    val isSelected: Boolean,
    val featuredImage: String
) : ClusterItem {

    constructor() : this(
        id = "",
        name = "",
        spot = FirebaseFirestore.getInstance().document(""),
        location = GeoPoint(0.0, 0.0),
        isSelected = false,
        featuredImage = ""
    )

    override fun getTitle() = name
    override fun getSnippet(): String? = null
    override fun getPosition() = LatLng(location.latitude, location.longitude)
}