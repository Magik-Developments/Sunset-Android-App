package com.madteam.sunset.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.SunsetTimeResponse
import com.madteam.sunset.repositories.SunsetRepository
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sunsetRepository: SunsetRepository
) : ViewModel() {

    private val _sunsetTimeInformation: MutableStateFlow<SunsetTimeResponse> =
        MutableStateFlow(SunsetTimeResponse())
    val sunsetTimeInformation: StateFlow<SunsetTimeResponse> = _sunsetTimeInformation

    init {
        getSunsetTimeBasedOnLocation()
    }

    private fun getSunsetTimeBasedOnLocation() {
        viewModelScope.launch {
            sunsetRepository.getSunsetTimeBasedOnLocation(
                latitude = 38.907192,
                longitude = -77.036873,
                timezone = "UTC"
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

}