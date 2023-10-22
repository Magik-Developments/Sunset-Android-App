package com.madteam.sunset.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.madteam.sunset.data.database.entities.UserProfileEntity

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile_table")
    suspend fun getAllUserProfileInfo(): UserProfileEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfileInfo(userProfileEntity: UserProfileEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserProfileInfo(userProfileEntity: UserProfileEntity)

    @Transaction
    suspend fun updateNonEmptyUserProfileFields(userProfileEntity: UserProfileEntity) {
        val currentInfo = getAllUserProfileInfo()
        val updatedInfo = UserProfileEntity(
            username = userProfileEntity.username,
            email = userProfileEntity.email,
            name = userProfileEntity.name,
            location = userProfileEntity.location,
            image = userProfileEntity.image.ifEmpty { currentInfo.image },
            admin = userProfileEntity.admin,
            provider = userProfileEntity.provider,
            creationDate = userProfileEntity.creationDate
        )
        updateUserProfileInfo(updatedInfo)
    }

}