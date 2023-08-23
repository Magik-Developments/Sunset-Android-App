package com.madteam.sunset.model

import com.google.firebase.firestore.GeoPoint

data class Spot(
    val id: String,
    val featuredImages: List<String>,
    val spottedBy: UserProfile,
    val creationDate: String,
    val name: String,
    val description: String,
    val score: Float,
    val visitedTimes: Int,
    val likes: Int,
    val locationInLatLng: GeoPoint,
    val location: String,
    val attributes: List<SpotAttribute>,
    val spotReviews: List<SpotReview>,
    val spotPosts: List<SpotPost>,
    val likedBy: List<String>
) {

    constructor() : this(
        "",
        listOf(),
        UserProfile(),
        "",
        "",
        "",
        0.0f,
        0,
        0,
        GeoPoint(0.0, 0.0),
        "",
        listOf<SpotAttribute>(),
        listOf(),
        listOf(),
        listOf()
    )

}