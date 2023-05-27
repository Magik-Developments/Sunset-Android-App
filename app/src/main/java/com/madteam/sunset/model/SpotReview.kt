package com.madteam.sunset.model

data class SpotReview(
    val id: String,
    val description: String,
    val title: String,
    val postedBy: UserProfile,
    val spotAttributes: List<SpotAttribute>,
    val creationDate: String,
    val score: Float
) {
    constructor() : this("", "", "", UserProfile(), listOf(), "", 0.0f)
}
