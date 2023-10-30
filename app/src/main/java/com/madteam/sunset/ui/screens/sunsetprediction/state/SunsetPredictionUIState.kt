package com.madteam.sunset.ui.screens.sunsetprediction.state

import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SunsetTimeResponse

data class SunsetPredictionUIState(
    val userLocation: LatLng = LatLng(0.0, 0.0),
    val userLocality: String = "",
    val sunsetTimeInformation: SunsetTimeResponse = SunsetTimeResponse(),
    val showLocationPermissionDialog: Boolean = false,
    val phasesInfoDialog: String = "",
    val sunsetScore: Int = -1,
    val sunsetTemperature: Double = 0.0,
    val qualityInfoDialog: Int = -1,
    val informationDate: String = "",
    val connectionError: Boolean = false
)