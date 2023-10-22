package com.madteam.sunset.data.model

import com.madteam.sunset.data.database.entities.UserProfileEntity

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

fun UserProfileEntity.toDomain() = UserProfile(
    username,
    email,
    provider,
    creationDate,
    name,
    location,
    image,
    admin
)

fun UserProfile.toEntity() = UserProfileEntity(
    username,
    email,
    provider,
    creation_date,
    name,
    location,
    image,
    admin
)