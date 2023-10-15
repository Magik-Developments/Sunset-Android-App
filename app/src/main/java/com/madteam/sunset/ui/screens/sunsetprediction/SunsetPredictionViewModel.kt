package com.madteam.sunset.ui.screens.sunsetprediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SunsetTimeResponse
import com.madteam.sunset.data.repositories.LocationRepository
import com.madteam.sunset.data.repositories.SunsetRepository
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.getTimezone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SunsetPredictionViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val sunsetRepository: SunsetRepository
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

    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
        getUserLocality()
        getSunsetTimeBasedOnLocation()
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

    private fun getUserLocality() {
        viewModelScope.launch {
            locationRepository.obtainLocalityFromLatLng(_userLocation.value).collectLatest {
                _userLocality.value = it
            }
        }
    }

    fun showLocationPermissionDialog(state: Boolean) {
        _showLocationPermissionDialog.value = state
    }

}