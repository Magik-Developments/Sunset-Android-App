package com.madteam.sunset.data.model

import com.madteam.sunset.data.database.entities.SpotAttributeEntity

data class SpotAttribute(
    val id: String,
    val description: String,
    val descriptionES: String,
    val descriptionCAT: String,
    val title: String,
    val titleES: String,
    val titleCAT: String,
    val icon: String,
    val type: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "")
}

fun SpotAttributeEntity.toDomain() = SpotAttribute(
    id = id,
    description = description,
    descriptionES = descriptionES,
    descriptionCAT = descriptionCAT,
    title = title,
    titleES = titleES,
    titleCAT = titleCAT,
    icon = icon,
    type = type
)

fun SpotAttribute.toEntity() = SpotAttributeEntity(
    id = id,
    description = description,
    descriptionES = descriptionES,
    descriptionCAT = descriptionCAT,
    title = title,
    titleES = titleES,
    titleCAT = titleCAT,
    icon = icon,
    type = type
)
