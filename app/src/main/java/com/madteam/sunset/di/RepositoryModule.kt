package com.madteam.sunset.di

import android.location.Geocoder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.madteam.sunset.api.SunsetApiService
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.AuthRepository
import com.madteam.sunset.repositories.DatabaseContract
import com.madteam.sunset.repositories.DatabaseRepository
import com.madteam.sunset.repositories.LocationContract
import com.madteam.sunset.repositories.LocationRepository
import com.madteam.sunset.repositories.SunsetContract
import com.madteam.sunset.repositories.SunsetRepository
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
        storage: FirebaseStorage
    ): DatabaseContract =
        DatabaseRepository(firestore, storage)

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