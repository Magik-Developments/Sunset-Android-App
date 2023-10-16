package com.madteam.sunset.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.madteam.sunset.api.SunsetApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
object ApplicationDependencies {

    @Provides
    @Named("SunriseSunset")
    fun providesSunriseSunsetRetrofit(
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.sunrisesunset.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Named("WeatherApi")
    fun providesWeatherApiRetrofit(
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesSunsetApiService(@Named("SunriseSunset") retrofit: Retrofit): SunsetApiService {
        return retrofit.create(SunsetApiService::class.java)
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(produceFile = {
            context.preferencesDataStoreFile(
                USER_PREFERENCES
            )
        })
    }


}