package com.madteam.sunset.ui.screens.sunsetprediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SunsetTimeResponse
import com.madteam.sunset.data.model.WeatherResponse
import com.madteam.sunset.data.repositories.SunsetRepository
import com.madteam.sunset.data.repositories.WeatherRepository
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
import javax.inject.Inject

@HiltViewModel
class SunsetPredictionViewModel @Inject constructor(
    private val sunsetRepository: SunsetRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _userLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val userLocation: StateFlow<LatLng> = _userLocation

    private val _userLocality: MutableStateFlow<String> = MutableStateFlow("")
    val userLocality: StateFlow<String> = _userLocality

    private val _sunsetTimeInformation: MutableStateFlow<SunsetTimeResponse> = MutableStateFlow(
        SunsetTimeResponse()
    )
    val sunsetTimeInformation: StateFlow<SunsetTimeResponse> = _sunsetTimeInformation

    private val _showLocationPermissionDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showLocationPermissionDialog: StateFlow<Boolean> = _showLocationPermissionDialog

    private val _phasesInfoDialog: MutableStateFlow<String> = MutableStateFlow("")
    val phasesInfoDialog: StateFlow<String> = _phasesInfoDialog

    private val _sunsetScore: MutableStateFlow<Int> = MutableStateFlow(0)
    val sunsetScore: StateFlow<Int> = _sunsetScore

    private val _weatherInfo: MutableStateFlow<WeatherResponse> =
        MutableStateFlow(WeatherResponse())

    private val _sunsetTemperature: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val sunsetTemperature: StateFlow<Double> = _sunsetTemperature

    private val _qualityInfoDialog: MutableStateFlow<Int> = MutableStateFlow(-1)
    val qualityInfoDialog: StateFlow<Int> = _qualityInfoDialog

    private val _informationDate: MutableStateFlow<String> = MutableStateFlow("")
    val informationDate: StateFlow<String> = _informationDate

    init {
        obtainActualDate()
    }

    private fun obtainActualDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()

        val formattedDate = dateFormat.format(date)
        _informationDate.value = formattedDate
    }

    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
        obtainActualDate()
        getSunsetTimeBasedOnLocation()
        getWeatherBasedOnLocationAndHour()
    }

    private fun getSunsetTimeBasedOnLocation() {
        viewModelScope.launch {
            sunsetRepository.getSunsetTimeBasedOnLocation(
                latitude = _userLocation.value.latitude,
                longitude = _userLocation.value.longitude,
                timezone = getTimezone(),
                date = _informationDate.value
            ).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        _sunsetTimeInformation.value = it.data!!
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getWeatherBasedOnLocationAndHour() {
        viewModelScope.launch {
            if (_userLocation.value.latitude != 0.0 && _userLocation.value.longitude != 0.0) {
                weatherRepository.getWeatherBasedOnLocationAndHour(
                    latitude = _userLocation.value.latitude,
                    longitude = _userLocation.value.longitude,
                    hour = 19,
                    key = "56a43d55a96942d4b3072635231610"
                ).collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            _weatherInfo.value = it.data!!
                            _sunsetTemperature.value =
                                calculateSunsetTemperature(
                                    _weatherInfo.value,
                                    _informationDate.value
                                )
                            _userLocality.value = _weatherInfo.value.location!!.name!!
                            _sunsetScore.value =
                                calculateSunsetScore(_weatherInfo.value, _informationDate.value)
                        }

                        is Resource.Error -> {
                            println(it.message)
                        }

                        is Resource.Loading -> {
                            println("loading")
                        }
                    }
                }
            }
        }
    }

    fun showLocationPermissionDialog(state: Boolean) {
        _showLocationPermissionDialog.value = state
    }

    fun setPhasesInfoDialog(phase: String) {
        _phasesInfoDialog.value = phase
    }

    fun setQualityInfoDialog(quality: Int) {
        _qualityInfoDialog.value = quality
    }

    fun setPreviousDayPrediction() {
        viewModelScope.launch {
            val originalDate = _informationDate.value
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            val date = dateFormat.parse(originalDate)

            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }
            calendar.add(Calendar.DAY_OF_MONTH, -1)

            val modifiedDate = dateFormat.format(calendar.time)

            _informationDate.value = modifiedDate
            getSunsetTimeBasedOnLocation()
            _sunsetScore.value = calculateSunsetScore(_weatherInfo.value, _informationDate.value)
            _sunsetTemperature.value =
                calculateSunsetTemperature(_weatherInfo.value, _informationDate.value)
        }
    }

    fun setNextDayPrediction() {
        viewModelScope.launch {
            val originalDate = _informationDate.value
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            val date = dateFormat.parse(originalDate)

            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Sumar un día

            val modifiedDate = dateFormat.format(calendar.time)

            _informationDate.value = modifiedDate
            getSunsetTimeBasedOnLocation()
            _sunsetScore.value = calculateSunsetScore(_weatherInfo.value, _informationDate.value)
            _sunsetTemperature.value =
                calculateSunsetTemperature(_weatherInfo.value, _informationDate.value)
        }
    }

}