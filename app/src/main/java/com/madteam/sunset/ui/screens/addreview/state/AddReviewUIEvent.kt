package com.madteam.sunset.ui.screens.addreview.state

import com.madteam.sunset.data.model.SpotAttribute

sealed class AddReviewUIEvent {
    data class ShowExitDialog(val state: Boolean) : AddReviewUIEvent()
    data class ShowFinishedDialog(val state: Boolean) : AddReviewUIEvent()
    data class UpdateReviewTitle(val text: String) : AddReviewUIEvent()
    data class UpdateReviewDescription(val text: String) : AddReviewUIEvent()
    data class UpdateReviewScore(val score: Float) : AddReviewUIEvent()
    data class UpdateSelectedAttribute(val attribute: SpotAttribute) : AddReviewUIEvent()
    data class CreateNewReview(val spotReference: String) : AddReviewUIEvent()
    data object ClearErrorToastText : AddReviewUIEvent()
    data object ClearUploadProgress : AddReviewUIEvent()

}
