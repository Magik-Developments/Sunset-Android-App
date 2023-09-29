package com.madteam.sunset.api

import com.madteam.sunset.data.model.SunsetTimeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SunsetApiService {
    @GET("json")
    suspend fun getSunsetTimeBasedOnLocation(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("timezone") timezone: String
    ): SunsetTimeResponse
}