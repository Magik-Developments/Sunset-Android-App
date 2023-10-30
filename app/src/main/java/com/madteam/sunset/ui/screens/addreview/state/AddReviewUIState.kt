package com.madteam.sunset.ui.screens.addreview.state

import androidx.annotation.StringRes
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.utils.Resource

data class AddReviewUIState(
    val attributesList: List<SpotAttribute> = listOf(),
    val selectedAttributes: List<SpotAttribute> = listOf(),
    val reviewTitle: String = "",
    val reviewDescription: String = "",
    val reviewScore: Int = 0,
    val showExitDialog: Boolean = false,
    val showFinishedDialog: Boolean = false,
    val uploadProgress: Resource<String> = Resource.Success(""),
    @StringRes val errorToastText: Int = -1
)