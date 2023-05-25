package com.madteam.sunset.model

data class SpotPost(
    val id: String,
    val description: String,
    val spot: Spot,
    val images: List<String>,
    val author: UserProfile,
    val creation_date: String
) {
    constructor() : this("", "", Spot(), listOf(), UserProfile(), "")
}
