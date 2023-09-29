package com.madteam.sunset.di

import android.location.Geocoder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.madteam.sunset.api.SunsetApiService
import com.madteam.sunset.data.database.dao.SpotAttributeDao
import com.madteam.sunset.data.repositories.AuthContract
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseContract
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.data.repositories.LocationContract
import com.madteam.sunset.data.repositories.LocationRepository
import com.madteam.sunset.data.repositories.SunsetContract
import com.madteam.sunset.data.repositories.SunsetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideFirebaseAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthContract = AuthRepository(firebaseAuth)

    @Provides
    fun provideDatabaseRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        spotAttributeDao: SpotAttributeDao
    ): DatabaseContract =
        DatabaseRepository(firestore, storage, spotAttributeDao)

    @Provides
    fun provideLocationRepository(
        geocoder: Geocoder
    ): LocationContract =
        LocationRepository(geocoder)

    @Provides
    fun provideSunsetRepository(
        apiService: SunsetApiService
    ): SunsetContract =
        SunsetRepository(apiService)
}