package com.madteam.sunset.di

import android.content.Context
import androidx.room.Room
import com.madteam.sunset.data.database.SpotAttributeDatabase
import com.madteam.sunset.data.database.UserProfileDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val SPOT_ATTRIBUTE_DATABASE_NAME = "spot_attribute_database"
    private const val USER_PROFILE_DATABASE_NAME = "user_profile_database"

    @Singleton
    @Provides
    @Named("SpotAttributes")
    fun providesSpotAttributesRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, SpotAttributeDatabase::class.java, SPOT_ATTRIBUTE_DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    @Named("UserProfile")
    fun providesUserProfileRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, UserProfileDatabase::class.java, USER_PROFILE_DATABASE_NAME
        ).build()


    @Singleton
    @Provides
    fun provideSpotAttributeDao(@Named("SpotAttributes") database: SpotAttributeDatabase) =
        database.getSpotAttributeDao()

    @Singleton
    @Provides
    fun provideUserProfileDao(@Named("UserProfile") database: UserProfileDatabase) =
        database.getUserProfileDao()
}