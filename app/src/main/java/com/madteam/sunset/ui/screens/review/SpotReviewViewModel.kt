package com.madteam.sunset.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.SpotReview
import com.madteam.sunset.repositories.DatabaseRepository
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

    private val _reviewInfo: MutableStateFlow<SpotReview> = MutableStateFlow(SpotReview())
    val reviewInfo: StateFlow<SpotReview> = _reviewInfo

    private val _reviewReference: MutableStateFlow<String> = MutableStateFlow("")
    private val _spotReference: MutableStateFlow<String> = MutableStateFlow("")

    fun setReviewReference(docReference: String) {
        _reviewReference.value = docReference
    }

    fun setSpotReference(docReference: String) {
        _spotReference.value = docReference
        getReviewInfo()
    }

    private fun getReviewInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotReviewByDocRef(_spotReference.value, _reviewReference.value)
                .collectLatest { review ->
                    _reviewInfo.value = review
                }
        }
    }
}