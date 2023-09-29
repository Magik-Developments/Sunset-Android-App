package com.madteam.sunset.data.model

data class PostComment(
    val id: String,
    val comment: String,
    val author: UserProfile,
    val creationDate: String
) {
    constructor() : this("", "", UserProfile(), "")
}
