package com.madteam.sunset.ui.screens.review.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.ui.screens.review.state.ReviewUIEvent
import com.madteam.sunset.ui.screens.review.state.ReviewUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotReviewViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _state: MutableStateFlow<ReviewUIState> = MutableStateFlow(ReviewUIState())
    val state: StateFlow<ReviewUIState> = _state

    fun onEvent(event: ReviewUIEvent) {
        when (event) {
            is ReviewUIEvent.SetSelectedAttributeInfoDialog -> {
                setSelectedAttrInfoDialog(event.attribute)
            }

            is ReviewUIEvent.SetShowAttributeInfoDialog -> {
                setShowAttrInfoDialog(event.show)
            }

            is ReviewUIEvent.SetReferences -> {
                setReferences(event.reviewReference, event.spotReference)
            }
        }
    }

    private fun setReferences(reviewReference: String, spotReference: String) {
        _state.value = _state.value.copy(
            reviewReference = reviewReference,
            spotReference = spotReference
        )
        viewModelScope.launch {
            getReviewInfo()
        }
    }

    private fun setShowAttrInfoDialog(show: Boolean) {
        _state.value = _state.value.copy(showAttrInfoDialog = show)
    }

    private fun setSelectedAttrInfoDialog(attribute: SpotAttribute) {
        _state.value = _state.value.copy(selectedAttrInfoDialog = attribute)
    }

    private fun getReviewInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotReviewByDocRef(
                _state.value.spotReference,
                _state.value.reviewReference
            )
                .collectLatest { review ->
                    _state.value = _state.value.copy(reviewInfo = review)
                }
        }
    }
}