package com.madteam.sunset.di

import android.location.Geocoder
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.madteam.sunset.api.SunsetApiService
import com.madteam.sunset.api.WeatherApiService
import com.madteam.sunset.data.database.dao.SpotAttributeDao
import com.madteam.sunset.data.database.dao.UserProfileDao
import com.madteam.sunset.data.repositories.AuthContract
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseContract
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.data.repositories.LocationContract
import com.madteam.sunset.data.repositories.LocationRepository
import com.madteam.sunset.data.repositories.SunsetContract
import com.madteam.sunset.data.repositories.SunsetRepository
import com.madteam.sunset.data.repositories.WeatherContract
import com.madteam.sunset.data.repositories.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providesFirebaseAuthRepository(
        firebaseAuth: FirebaseAuth,
        googleSignInClient: GoogleSignInClient
    ): AuthContract = AuthRepository(firebaseAuth, googleSignInClient)

    @Provides
    fun providesDatabaseRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        spotAttributeDao: SpotAttributeDao,
        userProfileDao: UserProfileDao
    ): DatabaseContract =
        DatabaseRepository(firestore, storage, spotAttributeDao, userProfileDao)

    @Provides
    fun providesLocationRepository(
        geocoder: Geocoder
    ): LocationContract =
        LocationRepository(geocoder)

    @Provides
    fun providesSunsetRepository(
        apiService: SunsetApiService
    ): SunsetContract =
        SunsetRepository(apiService)

    @Provides
    fun providesWeatherRepository(
        apiService: WeatherApiService
    ): WeatherContract =
        WeatherRepository(apiService)
}