package com.madteam.sunset.data.repositories

import android.util.Log
import com.madteam.sunset.api.WeatherApiService
import com.madteam.sunset.data.model.WeatherResponse
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: WeatherApiService
) : WeatherContract {
    override fun getWeatherBasedOnLocationAndHour(
        latitude: Double,
        longitude: Double,
        hour: Int,
        key: String
    ): Flow<Resource<WeatherResponse>> = flow<Resource<WeatherResponse>> {
        emit(Resource.Loading())
        val response =
            apiService.getWeatherBasedOnLocationAndHour(key, "$latitude,$longitude", hour)
        emit(Resource.Success(response))
    }.catch { exception ->
        Log.e("WeatherRepository::getWeatherBasedOnLocationAndHour", "Error: ${exception.message}")
        emit(Resource.Error(exception.message.toString()))
    }
}

interface WeatherContract {
    fun getWeatherBasedOnLocationAndHour(
        latitude: Double,
        longitude: Double,
        hour: Int,
        key: String
    ): Flow<Resource<WeatherResponse>>
}