package com.madteam.sunset.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madteam.sunset.data.database.entities.SpotAttributeEntity

@Dao
interface SpotAttributeDao {

    @Query("SELECT * FROM spot_attributes_table")
    suspend fun getAllSpotAttributes(): List<SpotAttributeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSpotAttributes(spotAttributes: List<SpotAttributeEntity>)
}