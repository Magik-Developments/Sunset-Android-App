package com.madteam.sunset.ui.screens.spotdetail.state

import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SpotAttribute

sealed class SpotDetailUIEvent {
    data class SetSpotReference(val docReference: String) : SpotDetailUIEvent()
    data class UpdateUserLocation(val latLng: LatLng) : SpotDetailUIEvent()
    data class SetShowReportDialog(val show: Boolean) : SpotDetailUIEvent()
    data class SetSelectedReportOption(val option: String) : SpotDetailUIEvent()
    data class SetAdditionalReportInformation(val info: String) : SpotDetailUIEvent()
    data class SetReportSent(val sent: Boolean) : SpotDetailUIEvent()
    data class SetShowAttrInfoDialog(val show: Boolean) : SpotDetailUIEvent()
    data class SetAttrSelectedDialog(val attribute: SpotAttribute) : SpotDetailUIEvent()
    data object ModifyUserSpotLike : SpotDetailUIEvent()
    data object SendReport : SpotDetailUIEvent()
}