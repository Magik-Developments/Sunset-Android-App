package com.madteam.sunset.ui.screens.spotdetail.state

import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.SpotAttribute

data class SpotDetailUIState(
    val spotInfo: Spot = Spot(),
    val isSpotLikedByUser: Boolean = false,
    val spotLikes: Int = 0,
    val userLocation: LatLng = LatLng(0.0, 0.0),
    val userIsAbleToEditOrRemoveSpot: Boolean = false,
    val showReportDialog: Boolean = false,
    val showReportSentDialog: Boolean = false,
    val availableOptionsToReport: List<String> = listOf(""),
    val selectedReportOption: String = "",
    val additionalReportInformation: String = "",
    val showAttrInfoDialog: Boolean = false,
    val attrSelectedDialog: SpotAttribute = SpotAttribute(),
    val reportSent: Boolean = false,
)