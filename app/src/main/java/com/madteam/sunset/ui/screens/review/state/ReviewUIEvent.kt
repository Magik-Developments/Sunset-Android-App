package com.madteam.sunset.ui.screens.review.state

import com.madteam.sunset.data.model.SpotAttribute

sealed class ReviewUIEvent {
    data class SetSelectedAttributeInfoDialog(val attribute: SpotAttribute) : ReviewUIEvent()
    data class SetShowAttributeInfoDialog(val show: Boolean) : ReviewUIEvent()
    data class SetReferences(val reviewReference: String, val spotReference: String) :
        ReviewUIEvent()
}