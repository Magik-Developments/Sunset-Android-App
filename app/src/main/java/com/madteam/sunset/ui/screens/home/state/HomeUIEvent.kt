package com.madteam.sunset.ui.screens.home.state

import com.google.android.gms.maps.model.LatLng

sealed class HomeUIEvent {
    data class UpdateUserLocation(val location: LatLng) : HomeUIEvent()
    data class ModifyUserSpotLike(val spotId: String) : HomeUIEvent()
    data class ModifyUserPostLike(val postId: String) : HomeUIEvent()
    data class ShowLocationPermissionDialog(val show: Boolean) : HomeUIEvent()
    data object LoadNextSpotsPage : HomeUIEvent()
    data object LoadNextPostsPage : HomeUIEvent()
}