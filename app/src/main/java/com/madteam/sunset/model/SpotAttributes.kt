package com.madteam.sunset.model

data class SpotAttributes(
    val id: String,
    val description: String,
    val title: String,
    val icon: String,
    val favorable: Boolean
) {
    constructor() : this("", "", "", "", false)
}
