package com.madteam.sunset.model

data class PostComment(
    val id: String,
    val comment: String,
    val author: UserProfile,
    val creationDate: String
) {
    constructor() : this("", "", UserProfile(), "")
}
