package com.madteam.sunset.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spot_attributes_table")
data class SpotAttributeEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "type") val type: String
)
