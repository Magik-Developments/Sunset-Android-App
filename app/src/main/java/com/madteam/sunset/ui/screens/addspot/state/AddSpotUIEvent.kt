package com.madteam.sunset.ui.screens.addspot.state

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SpotAttribute

sealed class AddSpotUIEvent {
    data class ImageSelected(val uri: Uri) : AddSpotUIEvent()
    data class UpdateSpotName(val text: String) : AddSpotUIEvent()
    data class UpdateSpotDescription(val text: String) : AddSpotUIEvent()
    data class AttributeClicked(val attribute: SpotAttribute) : AddSpotUIEvent()
    data class UpdateSpotScore(val score: Float) : AddSpotUIEvent()
    data class ShowExitDialog(val state: Boolean) : AddSpotUIEvent()
    data class UpdateSpotLocation(val location: LatLng) : AddSpotUIEvent()
    data class UpdateSelectedImages(val uris: List<Uri>) : AddSpotUIEvent()
    data object DeleteSelectedImage : AddSpotUIEvent()
    data object ClearUploadProgress : AddSpotUIEvent()
    data object ClearErrorToastText : AddSpotUIEvent()
    data object CreateNewSpot : AddSpotUIEvent()
    data object ObtainCountryAndLocality : AddSpotUIEvent()
}