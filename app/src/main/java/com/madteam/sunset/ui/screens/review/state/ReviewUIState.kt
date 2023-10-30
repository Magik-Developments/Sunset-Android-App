package com.madteam.sunset.ui.screens.review.state

import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.model.SpotReview

data class ReviewUIState(
    val reviewInfo: SpotReview = SpotReview(),
    val reviewReference: String = "",
    val spotReference: String = "",
    val showAttrInfoDialog: Boolean = false,
    val selectedAttrInfoDialog: SpotAttribute = SpotAttribute()
)