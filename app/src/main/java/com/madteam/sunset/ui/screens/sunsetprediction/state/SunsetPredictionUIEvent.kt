package com.madteam.sunset.ui.screens.sunsetprediction.state

import com.google.android.gms.maps.model.LatLng

sealed class SunsetPredictionUIEvent {
    data class ShowLocationPermissionDialog(val show: Boolean) : SunsetPredictionUIEvent()
    data class UpdateUserLocation(val location: LatLng) : SunsetPredictionUIEvent()
    data class SetPhasesInfoDialog(val phasesInfo: String) : SunsetPredictionUIEvent()
    data class SetQualityInfoDialog(val qualityInfo: Int) : SunsetPredictionUIEvent()
    data object SetPreviousDayPrediction : SunsetPredictionUIEvent()
    data object SetNextDayPrediction : SunsetPredictionUIEvent()

}