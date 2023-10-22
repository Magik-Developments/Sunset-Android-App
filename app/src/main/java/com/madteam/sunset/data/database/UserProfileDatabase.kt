package com.madteam.sunset.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.madteam.sunset.data.database.dao.UserProfileDao
import com.madteam.sunset.data.database.entities.UserProfileEntity

@Database(entities = [UserProfileEntity::class], version = 1)
abstract class UserProfileDatabase : RoomDatabase() {
    abstract fun getUserProfileDao(): UserProfileDao
}