package com.madteam.sunset.ui.screens.sunsetprediction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.BuildConfig
import com.madteam.sunset.data.model.WeatherResponse
import com.madteam.sunset.data.repositories.SunsetRepository
import com.madteam.sunset.data.repositories.WeatherRepository
import com.madteam.sunset.ui.screens.sunsetprediction.state.SunsetPredictionUIEvent
import com.madteam.sunset.ui.screens.sunsetprediction.state.SunsetPredictionUIState
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.calculateSunsetScore
import com.madteam.sunset.utils.calculateSunsetTemperature
import com.madteam.sunset.utils.getTimezone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val DATE_FORMAT = "yyyy-MM-dd"

@HiltViewModel
class SunsetPredictionViewModel @Inject constructor(
    private val sunsetRepository: SunsetRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _state: MutableStateFlow<SunsetPredictionUIState> = MutableStateFlow(
        SunsetPredictionUIState()
    )
    val state: StateFlow<SunsetPredictionUIState> = _state

    private val _weatherInfo: MutableStateFlow<WeatherResponse> =
        MutableStateFlow(WeatherResponse())

    init {
        obtainActualDate()
    }

    fun onEvent(event: SunsetPredictionUIEvent) {
        when (event) {
            is SunsetPredictionUIEvent.SetNextDayPrediction -> {
                setNextDayPrediction()
            }

            is SunsetPredictionUIEvent.SetPhasesInfoDialog -> {
                setPhasesInfoDialog(event.phasesInfo)
            }

            is SunsetPredictionUIEvent.SetPreviousDayPrediction -> {
                setPreviousDayPrediction()
            }

            is SunsetPredictionUIEvent.SetQualityInfoDialog -> {
                setQualityInfoDialog(event.qualityInfo)
            }

            is SunsetPredictionUIEvent.ShowLocationPermissionDialog -> {
                showLocationPermissionDialog(event.show)
            }

            is SunsetPredictionUIEvent.UpdateUserLocation -> {
                updateUserLocation(event.location)
            }
        }
    }

    private fun obtainActualDate() {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
        val date = Date()

        val formattedDate = dateFormat.format(date)
        _state.value = _state.value.copy(informationDate = formattedDate)
    }

    fun updateUserLocation(location: LatLng) {
        _state.value = _state.value.copy(userLocation = location)
        obtainActualDate()
        getSunsetTimeBasedOnLocation()
        getWeatherBasedOnLocationAndHour()
    }

    private fun getSunsetTimeBasedOnLocation() {
        viewModelScope.launch {
            sunsetRepository.getSunsetTimeBasedOnLocation(
                latitude = _state.value.userLocation.latitude,
                longitude = _state.value.userLocation.longitude,
                timezone = getTimezone(),
                date = _state.value.informationDate
            ).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(sunsetTimeInformation = it.data!!)
                        _state.value = _state.value.copy(connectionError = false)
                    }

                    else -> {
                        //Not necessary
                    }
                }
            }
        }
    }

    private fun getWeatherBasedOnLocationAndHour() {
        viewModelScope.launch {
            if (_state.value.userLocation.latitude != 0.0 && _state.value.userLocation.longitude != 0.0) {
                weatherRepository.getWeatherBasedOnLocationAndHour(
                    latitude = _state.value.userLocation.latitude,
                    longitude = _state.value.userLocation.longitude,
                    hour = 19,
                    key = BuildConfig.WEATHER_API_KEY
                ).collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            _weatherInfo.value = it.data!!
                            _state.value = _state.value.copy(
                                sunsetTemperature = calculateSunsetTemperature(
                                    _weatherInfo.value,
                                    _state.value.informationDate
                                ),
                                userLocality = _weatherInfo.value.location!!.name!!,
                                sunsetScore = calculateSunsetScore(
                                    _weatherInfo.value,
                                    _state.value.informationDate
                                ),
                                connectionError = false
                            )
                        }

                        is Resource.Error -> {
                            _state.value = _state.value.copy(connectionError = true)
                        }

                        is Resource.Loading -> {
                            _state.value = _state.value.copy(connectionError = false)
                        }
                    }
                }
            }
        }
    }

    private fun showLocationPermissionDialog(state: Boolean) {
        _state.value = _state.value.copy(showLocationPermissionDialog = state)
    }

    private fun setPhasesInfoDialog(phase: String) {
        _state.value = _state.value.copy(phasesInfoDialog = phase)
    }

    private fun setQualityInfoDialog(quality: Int) {
        _state.value = _state.value.copy(qualityInfoDialog = quality)
    }

    private fun setPreviousDayPrediction() {
        viewModelScope.launch {
            val originalDate = _state.value.informationDate
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)

            val date = dateFormat.parse(originalDate)

            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }
            calendar.add(Calendar.DAY_OF_MONTH, -1)

            val modifiedDate = dateFormat.format(calendar.time)

            _state.value = _state.value.copy(informationDate = modifiedDate)
            getSunsetTimeBasedOnLocation()
            _state.value = _state.value.copy(
                sunsetScore = calculateSunsetScore(
                    _weatherInfo.value,
                    _state.value.informationDate
                ),
                sunsetTemperature = calculateSunsetTemperature(
                    _weatherInfo.value,
                    _state.value.informationDate
                )
            )
        }
    }

    private fun setNextDayPrediction() {
        viewModelScope.launch {
            val originalDate = _state.value.informationDate
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)

            val date = dateFormat.parse(originalDate)

            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1)

            val modifiedDate = dateFormat.format(calendar.time)

            _state.value = _state.value.copy(informationDate = modifiedDate)
            getSunsetTimeBasedOnLocation()
            _state.value = _state.value.copy(
                sunsetScore = calculateSunsetScore(
                    _weatherInfo.value,
                    _state.value.informationDate
                ),
                sunsetTemperature = calculateSunsetTemperature(
                    _weatherInfo.value,
                    _state.value.informationDate
                )
            )
        }
    }

}