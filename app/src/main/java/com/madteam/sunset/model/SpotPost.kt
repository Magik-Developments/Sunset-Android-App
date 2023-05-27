package com.madteam.sunset.model

data class SpotPost(
    val id: String,
    val description: String,
    val spotRef: String,
    val images: List<String>,
    val author: UserProfile,
    val creation_date: String
) {
    constructor() : this("", "", "", listOf(), UserProfile(), "")
}