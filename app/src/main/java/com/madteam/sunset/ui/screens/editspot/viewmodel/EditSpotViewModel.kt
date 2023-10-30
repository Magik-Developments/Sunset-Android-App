package com.madteam.sunset.ui.screens.editspot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.data.repositories.LocationRepository
import com.madteam.sunset.domain.usecases.GetSpotAttributesUseCase
import com.madteam.sunset.ui.screens.editspot.state.EditSpotUIEvent
import com.madteam.sunset.ui.screens.editspot.state.EditSpotUIState
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class EditSpotViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository,
    private val getSpotAttributesUseCase: GetSpotAttributesUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<EditSpotUIState> = MutableStateFlow(EditSpotUIState())
    val state: StateFlow<EditSpotUIState> = _state

    private val _spotReference: MutableStateFlow<String> = MutableStateFlow("")

    private val _username: MutableStateFlow<String> = MutableStateFlow("")

    init {
        getSpotAttributesList()
        getUserInfo()
    }

    fun onEvent(event: EditSpotUIEvent) {
        when (event) {
            is EditSpotUIEvent.ClearErrorToastText -> {
                clearErrorToastText()
            }

            is EditSpotUIEvent.ClearUploadProgress -> {
                clearUploadProgressState()
            }

            is EditSpotUIEvent.DeleteSpot -> {
                deleteSpotIntent()
            }

            is EditSpotUIEvent.ShowDeleteDialog -> {
                setShowDeleteDialog(event.show)
            }

            is EditSpotUIEvent.ShowExitDialog -> {
                setShowExitDialog(event.show)
            }

            is EditSpotUIEvent.UpdateDescription -> {
                modifySpotDescription(event.description)
            }

            is EditSpotUIEvent.UpdateScore -> {
                modifyReviewScore(event.score)
            }

            is EditSpotUIEvent.UpdateSelectedAttributes -> {
                modifySelectedAttributes(event.attributes)
            }

            is EditSpotUIEvent.UpdateTitle -> {
                modifySpotTitle(event.title)
            }

            is EditSpotUIEvent.GetCountryAndLocality -> {
                obtainCountryAndCityFromLatLng()
            }

            is EditSpotUIEvent.SetSpotReference -> {
                setSpotReference(event.spotReference)
            }

            is EditSpotUIEvent.UpdateLocation -> {
                modifySpotLocation(event.location)
            }

            is EditSpotUIEvent.UpdateSpot -> {
                updateSpotIntent()
            }
        }
    }

    private fun setSpotReference(docReference: String) {
        _spotReference.value = docReference
        getSpotInfo()
    }

    private fun getSpotInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotByDocRef(_spotReference.value).collectLatest { spot ->
                _state.value = _state.value.copy(
                    spotInfo = spot,
                    spotTitle = spot.name,
                    imageUris = spot.featuredImages,
                    spotDescription = spot.description,
                    spotScore = spot.score.roundToInt(),
                    selectedAttributes = spot.attributes,
                    spotLocation = LatLng(
                        spot.locationInLatLng.latitude,
                        spot.locationInLatLng.longitude
                    )
                )
            }
        }
    }

    private fun getSpotAttributesList() {
        viewModelScope.launch {
            val result = getSpotAttributesUseCase.invoke()
            if (result.isNotEmpty()) {
                _state.value = _state.value.copy(
                    attributesList = result
                )
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                _username.value = databaseRepository.getUserByEmail(user.email!!).username
            }
        }
    }

    private fun modifyReviewScore(score: Float) {
        _state.value = _state.value.copy(
            spotScore = score.roundToInt()
        )
    }

    private fun modifySelectedAttributes(attribute: SpotAttribute) {
        if (!_state.value.selectedAttributes.contains(attribute)) {
            _state.value = _state.value.copy(
                selectedAttributes = _state.value.selectedAttributes + attribute
            )
        } else {
            _state.value = _state.value.copy(
                selectedAttributes = _state.value.selectedAttributes - attribute
            )
        }
    }

    private fun modifySpotTitle(title: String) {
        _state.value = _state.value.copy(
            spotTitle = title
        )
    }

    private fun modifySpotDescription(description: String) {
        _state.value = _state.value.copy(
            spotDescription = description
        )
    }

    private fun modifySpotLocation(location: LatLng) {
        _state.value = _state.value.copy(
            spotLocation = location
        )
    }

    private fun clearUploadProgressState() {
        _state.value = _state.value.copy(
            uploadProgress = Resource.Success("")
        )
    }

    private fun clearErrorToastText() {
        _state.value = _state.value.copy(
            errorToastText = -1
        )
    }

    private fun setShowExitDialog(state: Boolean) {
        _state.value = _state.value.copy(
            showExitDialog = state
        )
    }

    private fun setShowDeleteDialog(state: Boolean) {
        _state.value = _state.value.copy(
            showDeleteDialog = state
        )
    }

    private fun obtainCountryAndCityFromLatLng() {
        viewModelScope.launch {
            locationRepository.obtainCountryFromLatLng(_state.value.spotLocation).collectLatest {
                _state.value = _state.value.copy(
                    spotLocationCountry = it
                )
            }
            locationRepository.obtainLocalityFromLatLng(_state.value.spotLocation).collectLatest {
                _state.value = _state.value.copy(
                    spotLocationLocality = it
                )
            }
        }
    }

    private fun updateSpotIntent() {
        viewModelScope.launch {
            databaseRepository.updateSpot(
                spotReference = _spotReference.value,
                spotTitle = _state.value.spotTitle,
                spotDescription = _state.value.spotDescription,
                spotLocation = _state.value.spotLocation,
                spotCountry = _state.value.spotLocationCountry,
                spotLocality = _state.value.spotLocationLocality,
                spotAttributes = _state.value.selectedAttributes,
                spotScore = _state.value.spotScore,
            ).collectLatest {
                _state.value = _state.value.copy(
                    uploadProgress = it
                )
            }
        }
    }

    private fun deleteSpotIntent() {
        viewModelScope.launch {
            databaseRepository.deleteSpot(
                _spotReference.value
            ).collectLatest {
                _state.value = _state.value.copy(
                    uploadProgress = it
                )
            }
        }
    }

}