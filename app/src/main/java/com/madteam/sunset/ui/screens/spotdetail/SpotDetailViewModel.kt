package com.madteam.sunset.ui.screens.spotdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.model.Spot
import com.madteam.sunset.repositories.AuthRepository
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotDetailViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _spotInfo: MutableStateFlow<Spot> = MutableStateFlow(Spot())
    val spotInfo: StateFlow<Spot> = _spotInfo

    private val _spotReference: MutableStateFlow<String> = MutableStateFlow("")

    private val _spotIsLiked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val spotIsLiked: StateFlow<Boolean> = _spotIsLiked

    private val _spotLikes: MutableStateFlow<Int> = MutableStateFlow(0)
    val spotLikes: StateFlow<Int> = _spotLikes

    private val _userLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val userLocation: StateFlow<LatLng> = _userLocation

    private val _userIsAbleToEditOrRemoveSpot: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val userIsAbleToEditOrRemoveSpot: StateFlow<Boolean> = _userIsAbleToEditOrRemoveSpot

    private val _showReportDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showReportDialog: StateFlow<Boolean> = _showReportDialog

    private val _availableOptionsToReport: MutableStateFlow<List<String>> = MutableStateFlow(
        listOf(
            "Spam",
            "Offensive content",
            "Wrong location",
            "Wrong information",
            "Others"
        )
    )
    val availableOptionsToReport: StateFlow<List<String>> = _availableOptionsToReport

    private val _selectedReportOption: MutableStateFlow<String> =
        MutableStateFlow(_availableOptionsToReport.value.first())
    val selectedReportOption: StateFlow<String> = _selectedReportOption

    private val _additionalReportInformation: MutableStateFlow<String> = MutableStateFlow("")
    val additionalReportInformation: StateFlow<String> = _additionalReportInformation

    private lateinit var username: String

    init {
        viewModelScope.launch {
            getUserUsername()
        }
    }

    private fun getUserUsername() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                databaseRepository.getUserByEmail(user.email!!) {
                    username = it.username
                    _userIsAbleToEditOrRemoveSpot.value = it.admin
                }
            }
        }
    }

    fun setSpotReference(docReference: String) {
        _spotReference.value = docReference
        getSpotInfo()
    }

    fun selectedReportOption(selectedOption: String) {
        _selectedReportOption.value = selectedOption
    }

    fun setAdditionalReportInformation(text: String) {
        _additionalReportInformation.value = text
    }

    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
    }

    fun setShowReportDialog(show: Boolean) {
        _showReportDialog.value = show
    }

    private fun getSpotInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotByDocRef(_spotReference.value).collect { spot ->
                _spotInfo.value = spot
                _spotIsLiked.value = spot.likedBy.contains(username)
                _spotLikes.value = spot.likes
            }
        }
    }

    fun modifyUserSpotLike() {
        viewModelScope.launch {
            databaseRepository.modifyUserSpotLike(_spotReference.value, username)
                .collectLatest {}
            if (_spotIsLiked.value) {
                _spotLikes.value--
                _spotIsLiked.value = false
            } else {
                _spotLikes.value++
                _spotIsLiked.value = true
            }
        }
    }
}