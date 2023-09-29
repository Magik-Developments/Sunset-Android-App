package com.madteam.sunset.data.repositories

import android.util.Log
import com.madteam.sunset.api.SunsetApiService
import com.madteam.sunset.data.model.SunsetTimeResponse
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SunsetRepository @Inject constructor(
    private val apiService: SunsetApiService
) : SunsetContract {
    override fun getSunsetTimeBasedOnLocation(
        latitude: Double,
        longitude: Double,
        timezone: String
    ): Flow<Resource<SunsetTimeResponse>> = flow<Resource<SunsetTimeResponse>> {
        val response = apiService.getSunsetTimeBasedOnLocation(latitude, longitude, timezone)
        emit(Resource.Success(response))
    }.catch { exception ->
        Log.e("SunsetRepository::getSunsetTimeBasedOnLocation", "Error: ${exception.message}")
        emit(Resource.Error(exception.message.toString()))
    }

}

interface SunsetContract {
    fun getSunsetTimeBasedOnLocation(
        latitude: Double,
        longitude: Double,
        timezone: String
    ): Flow<Resource<SunsetTimeResponse>>
}