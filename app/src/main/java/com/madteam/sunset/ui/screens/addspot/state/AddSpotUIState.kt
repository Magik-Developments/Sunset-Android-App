package com.madteam.sunset.ui.screens.addspot.state

import android.net.Uri
import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.googlemaps.MapState

data class AddSpotUIState(
    val spotName: String = "",
    val spotDescription: String = "",
    val spotScore: Int = 0,
    val imageUris: List<Uri> = listOf(),
    val selectedImageUri: Uri = Uri.EMPTY,
    val spotLocation: LatLng = LatLng(0.0, 0.0),
    val spotLocationLocality: String = "",
    val spotLocationCountry: String = "",
    val mapState: MapState = MapState(),
    val attributesList: List<SpotAttribute> = listOf(),
    val selectedAttributes: List<SpotAttribute> = listOf(),
    val showExitDialog: Boolean = false,
    val uploadProgress: Resource<String> = Resource.Success(""),
    @StringRes val errorToastText: Int = -1
)