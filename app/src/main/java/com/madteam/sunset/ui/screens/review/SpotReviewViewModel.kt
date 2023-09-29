package com.madteam.sunset.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.model.SpotReview
import com.madteam.sunset.data.repositories.DatabaseRepository
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

    private val _showAttrInfoDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showAttrInfoDialog: StateFlow<Boolean> = _showAttrInfoDialog

    private val _selectedAttrInfoDialog: MutableStateFlow<SpotAttribute> =
        MutableStateFlow(SpotAttribute())
    val selectedAttrInfoDialog: StateFlow<SpotAttribute> = _selectedAttrInfoDialog

    fun setReferences(reviewReference: String, spotReference: String) {
        _reviewReference.value = reviewReference
        _spotReference.value = spotReference
        viewModelScope.launch {
            getReviewInfo()
        }
    }

    fun setShowAttrInfoDialog(show: Boolean) {
        _showAttrInfoDialog.value = show
    }

    fun setSelectedAttrInfoDialog(attribute: SpotAttribute) {
        _selectedAttrInfoDialog.value = attribute
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