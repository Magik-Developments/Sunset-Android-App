package com.madteam.sunset.data.model

data class UserProfile(
    val username: String,
    val email: String,
    val provider: String,
    val creation_date: String,
    val name: String,
    val location: String,
    val image: String,
    val admin: Boolean
) {

    constructor() : this("", "", "", "", "", "", "", false)
}