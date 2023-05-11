package com.madteam.sunset.model

data class UserProfile(
    val username: String,
    val email: String,
    val provider: String,
    val creation_date: String,
    val name: String,
    val location: String
) {

    constructor() : this("", "", "", "", "", "")
}