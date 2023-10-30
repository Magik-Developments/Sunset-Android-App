package com.madteam.sunset.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile_table")
data class UserProfileEntity(
    @PrimaryKey @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "provider") var provider: String,
    @ColumnInfo(name = "creation_date") var creationDate: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "location") var location: String,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "admin") var admin: Boolean
) {
    constructor() : this("", "", "", "", "", "", "", false)
}
