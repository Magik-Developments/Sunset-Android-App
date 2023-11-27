package com.madteam.sunset.ui.screens.home.state

import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.SpotPost
import com.madteam.sunset.data.model.SunsetTimeResponse
import com.madteam.sunset.data.model.UserProfile

data class HomeUIState(
    val sunsetTimeInformation: SunsetTimeResponse = SunsetTimeResponse(),
    val userLocation: LatLng = LatLng(0.0, 0.0),
    val userLocality: String = "",
    val remainingTimeToSunset: String = "",
    val spotsList: List<Spot> = listOf(),
    val postsList: List<SpotPost> = listOf(),
    val userInfo: UserProfile = UserProfile(),
    val showLocationPermissionDialog: Boolean = false
)
