package com.madteam.sunset.ui.screens.spotdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.ui.screens.spotdetail.state.SpotDetailUIEvent
import com.madteam.sunset.ui.screens.spotdetail.state.SpotDetailUIState
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

    private val _state: MutableStateFlow<SpotDetailUIState> = MutableStateFlow(SpotDetailUIState())
    val state: StateFlow<SpotDetailUIState> = _state

    private val _spotReference: MutableStateFlow<String> = MutableStateFlow("")

    private var username: String = ""

    init {
        viewModelScope.launch {
            getUserUsername()
        }
    }

    fun onEvent(event: SpotDetailUIEvent) {
        when (event) {
            is SpotDetailUIEvent.SetSpotReference -> {
                setSpotReference(event.docReference)
            }

            is SpotDetailUIEvent.UpdateUserLocation -> {
                updateUserLocation(event.latLng)
            }

            is SpotDetailUIEvent.SetShowReportDialog -> {
                setShowReportDialog(event.show)
            }

            is SpotDetailUIEvent.SetSelectedReportOption -> {
                selectedReportOption(event.option)
            }

            is SpotDetailUIEvent.SetAdditionalReportInformation -> {
                setAdditionalReportInformation(event.info)
            }

            is SpotDetailUIEvent.SetReportSent -> {
                setReportSentDialog(event.sent)
            }

            is SpotDetailUIEvent.SetShowAttrInfoDialog -> {
                setShowAttrInfoDialog(event.show)
            }

            is SpotDetailUIEvent.SetAttrSelectedDialog -> {
                setAttrSelectedDialog(event.attribute)
            }

            is SpotDetailUIEvent.ModifyUserSpotLike -> {
                modifyUserSpotLike()
            }

            is SpotDetailUIEvent.SendReport -> {
                sendReportIntent()
            }
        }
    }

    private fun getUserUsername() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                val userInfo = databaseRepository.getUserByEmail(user.email!!)
                username = userInfo.username
                _state.value = _state.value.copy(
                    userIsAbleToEditOrRemoveSpot = userInfo.admin
                )
            }
        }
    }

    private fun setSpotReference(docReference: String) {
        _spotReference.value = docReference
        getSpotInfo()
    }

    private fun setShowAttrInfoDialog(show: Boolean) {
        _state.value = _state.value.copy(showAttrInfoDialog = show)
    }

    private fun setAttrSelectedDialog(attribute: SpotAttribute) {
        _state.value = _state.value.copy(attrSelectedDialog = attribute)
    }

    private fun selectedReportOption(selectedOption: String) {
        _state.value = _state.value.copy(selectedReportOption = selectedOption)
    }

    private fun setReportSentDialog(status: Boolean) {
        _state.value = _state.value.copy(reportSent = status)
    }

    private fun setAdditionalReportInformation(text: String) {
        _state.value = _state.value.copy(additionalReportInformation = text)
    }

    private fun updateUserLocation(location: LatLng) {
        _state.value = _state.value.copy(userLocation = location)
    }

    private fun setShowReportDialog(show: Boolean) {
        _state.value = _state.value.copy(showReportDialog = show)
        getReportOptions()
    }

    private fun getReportOptions() {
        viewModelScope.launch {
            databaseRepository.getSpotReportsOptions().collectLatest {
                _state.value = _state.value.copy(availableOptionsToReport = it)
                _state.value = _state.value.copy(selectedReportOption = it.firstOrNull() ?: "Other")
            }
        }
    }

    private fun getSpotInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotByDocRef(_spotReference.value).collect { spot ->
                _state.value = _state.value.copy(
                    spotInfo = spot,
                    isSpotLikedByUser = spot.likedBy.contains(username.lowercase()),
                    spotLikes = spot.likes
                )
            }
        }
    }

    private fun modifyUserSpotLike() {
        viewModelScope.launch {
            databaseRepository.modifyUserSpotLike(_spotReference.value, username)
                .collectLatest {}
            if (_state.value.isSpotLikedByUser) {
                _state.value = _state.value.copy(
                    spotLikes = _state.value.spotLikes - 1,
                    isSpotLikedByUser = false
                )
            } else {
                _state.value = _state.value.copy(
                    spotLikes = _state.value.spotLikes + 1,
                    isSpotLikedByUser = true
                )
            }
        }
    }

    private fun sendReportIntent() {
        viewModelScope.launch {
            databaseRepository.sendReport(
                reportType = "Spot",
                reporterUsername = username,
                reportIssue = _state.value.selectedReportOption,
                reportDescription = _state.value.additionalReportInformation,
                documentReference = _spotReference.value
            ).collectLatest { }
        }
    }
}