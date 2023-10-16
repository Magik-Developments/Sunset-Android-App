package com.madteam.sunset.api

import com.madteam.sunset.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json")
    suspend fun getWeatherBasedOnLocationAndHour(
        @Query("key") key: String,
        @Query("q") coordinates: String,
        @Query("hour") hour: Int
    ): WeatherResponse
}