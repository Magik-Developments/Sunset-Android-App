package com.madteam.sunset.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.clustering.ClusterItem

data class SpotClusterItem(
    val id: String,
    val spotReference: DocumentReference,
    val spot: Spot,
    val isSelected: Boolean
) : ClusterItem {

    constructor() : this(
        id = "",
        spotReference = FirebaseFirestore.getInstance().document(""),
        spot = Spot(),
        isSelected = false
    )

    override fun getTitle() = spot.name
    override fun getSnippet(): String? = null
    override fun getPosition() =
        LatLng(spot.locationInLatLng.latitude, spot.locationInLatLng.longitude)
}