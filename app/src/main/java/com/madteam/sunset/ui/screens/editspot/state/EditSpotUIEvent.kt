package com.madteam.sunset.ui.screens.editspot.state

import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SpotAttribute

sealed class EditSpotUIEvent {
    data class UpdateTitle(val title: String) : EditSpotUIEvent()
    data class UpdateDescription(val description: String) : EditSpotUIEvent()
    data class UpdateSelectedAttributes(val attributes: SpotAttribute) : EditSpotUIEvent()
    data class UpdateLocation(val location: LatLng) : EditSpotUIEvent()
    data class ShowExitDialog(val show: Boolean) : EditSpotUIEvent()
    data class UpdateScore(val score: Float) : EditSpotUIEvent()
    data class ShowDeleteDialog(val show: Boolean) : EditSpotUIEvent()
    data class SetSpotReference(val spotReference: String) : EditSpotUIEvent()
    data object ClearUploadProgress : EditSpotUIEvent()
    data object ClearErrorToastText : EditSpotUIEvent()
    data object DeleteSpot : EditSpotUIEvent()
    data object GetCountryAndLocality : EditSpotUIEvent()
    data object UpdateSpot : EditSpotUIEvent()
}
