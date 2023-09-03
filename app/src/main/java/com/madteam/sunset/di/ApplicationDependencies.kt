package com.madteam.sunset.di

import com.madteam.sunset.api.SunsetApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApplicationDependencies {

    @Provides
    fun providesRetrofit(
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.sunrisesunset.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesSunsetApiService(retrofit: Retrofit): SunsetApiService {
        return retrofit.create(SunsetApiService::class.java)
    }

}