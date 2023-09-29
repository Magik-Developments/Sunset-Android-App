package com.madteam.sunset.data.model

data class SpotPost(
    val id: String,
    val description: String,
    val spotRef: String,
    val images: List<String>,
    val author: UserProfile,
    val creation_date: String,
    val comments: List<PostComment>,
    val likedBy: List<String>,
    val likes: Int
) {
    constructor() : this("", "", "", listOf(), UserProfile(), "", listOf(), listOf(), 0)
}
