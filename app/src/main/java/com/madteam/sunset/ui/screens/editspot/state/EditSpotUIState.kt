package com.madteam.sunset.ui.screens.editspot.state

import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.googlemaps.MapState

data class EditSpotUIState(
    val imageUris: List<String> = listOf(),
    val selectedImageUri: String = "",
    val spotTitle: String = "",
    val spotDescription: String = "",
    val spotLocation: LatLng = LatLng(0.0, 0.0),
    val spotInfo: Spot = Spot(),
    val mapState: MapState = MapState(),
    val spotLocationLocality: String = "",
    val spotLocationCountry: String = "",
    val attributesList: List<SpotAttribute> = mutableListOf(),
    val selectedAttributes: List<SpotAttribute> = mutableListOf(),
    val spotScore: Int = 0,
    val showExitDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    @StringRes val errorToastText: Int = -1,
    val uploadProgress: Resource<String> = Resource.Success("")
)
