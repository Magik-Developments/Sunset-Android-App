package com.madteam.sunset.di

import android.content.Context
import androidx.room.Room
import com.madteam.sunset.data.database.SpotAttributeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val SPOT_ATTRIBUTE_DATABASE_NAME = "spot_attribute_database"

    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, SpotAttributeDatabase::class.java, SPOT_ATTRIBUTE_DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideSpotAttributeDao(database: SpotAttributeDatabase) = database.getSpotAttributeDao()
}