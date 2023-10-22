package com.madteam.sunset.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madteam.sunset.data.database.entities.UserProfileEntity

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile_table")
    suspend fun getAllUserProfileInfo(): UserProfileEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfileInfo(userProfileEntity: UserProfileEntity)
}