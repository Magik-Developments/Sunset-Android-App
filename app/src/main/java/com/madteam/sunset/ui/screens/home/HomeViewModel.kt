package com.madteam.sunset.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.model.Spot
import com.madteam.sunset.model.SunsetTimeResponse
import com.madteam.sunset.repositories.DatabaseRepository
import com.madteam.sunset.repositories.LocationRepository
import com.madteam.sunset.repositories.SunsetRepository
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.calculateTimeDifference
import com.madteam.sunset.utils.convertHourToMilitaryFormat
import com.madteam.sunset.utils.getTimezone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sunsetRepository: SunsetRepository,
    private val locationRepository: LocationRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _sunsetTimeInformation: MutableStateFlow<SunsetTimeResponse> =
        MutableStateFlow(SunsetTimeResponse())
    val sunsetTimeInformation: StateFlow<SunsetTimeResponse> = _sunsetTimeInformation

    private val _userLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val userLocation: StateFlow<LatLng> = _userLocation

    private val _userLocality: MutableStateFlow<String> = MutableStateFlow("")
    val userLocality: StateFlow<String> = _userLocality

    private val _remainingTimeToSunset: MutableStateFlow<String> = MutableStateFlow("")
    val remainingTimeToSunset: StateFlow<String> = _remainingTimeToSunset

    private val _spotsList: MutableStateFlow<List<Spot>> = MutableStateFlow(listOf())
    val spotsList: StateFlow<List<Spot>> = _spotsList

    init {
        getSpotsList()
    }

    private fun getSpotsList() {
        viewModelScope.launch {
            databaseRepository.getAllSpots().collectLatest {
                _spotsList.value = it
            }
        }
    }

    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
        getUserLocality()
        getSunsetTimeBasedOnLocation()
        updateRemainingTimeToSunset()
    }

    private fun getUserLocality() {
        viewModelScope.launch {
            locationRepository.obtainLocalityFromLatLng(_userLocation.value).collectLatest {
                _userLocality.value = it
            }
        }
    }

    private fun getSunsetTimeBasedOnLocation() {
        viewModelScope.launch {
            sunsetRepository.getSunsetTimeBasedOnLocation(
                latitude = _userLocation.value.latitude,
                longitude = _userLocation.value.longitude,
                timezone = getTimezone()
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

    private fun updateRemainingTimeToSunset() {
        viewModelScope.launch {
            while (true) {
                _remainingTimeToSunset.value =
                    calculateTimeDifference(convertHourToMilitaryFormat(_sunsetTimeInformation.value.results.sunset))
                delay(60000)
            }
        }
    }

}