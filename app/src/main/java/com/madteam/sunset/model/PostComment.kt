package com.madteam.sunset.model

data class PostComment(
    val id: String,
    val comment: String,
    val author: UserProfile,
    val creation_date: String
) {
    constructor() : this("", "", UserProfile(), "")
}
