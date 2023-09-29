package com.madteam.sunset.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.madteam.sunset.data.database.dao.SpotAttributeDao
import com.madteam.sunset.data.database.entities.SpotAttributeEntity

@Database(entities = [SpotAttributeEntity::class], version = 1)
abstract class SpotAttributeDatabase : RoomDatabase() {

    abstract fun getSpotAttributeDao(): SpotAttributeDao

}