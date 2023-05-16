package com.madteam.sunset.model

data class Spot(
    val name: String,
    val location: String,
    val image: String,
) {

    constructor() : this( "", "", "")
}