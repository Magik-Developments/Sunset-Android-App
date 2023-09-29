package com.madteam.sunset.data.model

import com.madteam.sunset.data.database.entities.SpotAttributeEntity

data class SpotAttribute(
    val id: String,
    val description: String,
    val title: String,
    val icon: String,
    val type: String
) {
    constructor() : this("", "", "", "", "")
}

fun SpotAttributeEntity.toDomain() = SpotAttribute(id, description, title, icon, type)
fun SpotAttribute.toEntity() = SpotAttributeEntity(id, description, title, icon, type)
