package com.madteam.sunset.model

data class SpotAttribute(
    val id: String,
    val description: String,
    val title: String,
    val icon: String,
    val type: String
) {
    constructor() : this("", "", "", "", "")
}
