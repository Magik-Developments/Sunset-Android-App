package com.madteam.sunset.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile_table")
data class UserProfileEntity(
    @PrimaryKey @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "provider") val provider: String,
    @ColumnInfo(name = "creation_date") val creationDate: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "admin") val admin: Boolean
) {
    constructor() : this("", "", "", "", "", "", "", false)
}
