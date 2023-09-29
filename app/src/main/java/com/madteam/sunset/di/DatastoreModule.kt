package com.madteam.sunset.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.madteam.sunset.data.database.datastore.DatastoreContract
import com.madteam.sunset.data.database.datastore.DatastoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Singleton
    @Provides
    fun providesDatastoreManager(
        datastore: DataStore<Preferences>
    ): DatastoreContract = DatastoreManager(
        datastore
    )
}