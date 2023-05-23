package com.madteam.sunset.model

import com.google.firebase.firestore.GeoPoint

data class Spot(
    val id: String,
    val spottedBy: UserProfile,
    val creationDate: String,
    val name: String,
    val description: String,
    val score: Float,
    val visitedTimes: Int,
    val likes: Int,
    val locationInLatLng: GeoPoint,
    val location: String,
    val attributes: SpotAttributes
) {

    constructor() : this(
        "",
        UserProfile(),
        "",
        "",
        "",
        0.0f,
        0,
        0,
        GeoPoint(0.0, 0.0),
        "",
        SpotAttributes()
    )

}