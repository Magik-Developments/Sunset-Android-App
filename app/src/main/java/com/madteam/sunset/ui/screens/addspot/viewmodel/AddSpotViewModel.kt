package com.madteam.sunset.ui.screens.addspot.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.R
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.data.repositories.LocationRepository
import com.madteam.sunset.domain.usecases.GetSpotAttributesUseCase
import com.madteam.sunset.ui.screens.addpost.ui.MAX_IMAGES_SELECTED
import com.madteam.sunset.ui.screens.addspot.state.AddSpotUIEvent
import com.madteam.sunset.ui.screens.addspot.state.AddSpotUIState
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class AddSpotViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository,
    private val getSpotAttributesUseCase: GetSpotAttributesUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<AddSpotUIState> = MutableStateFlow(AddSpotUIState())
    val state: StateFlow<AddSpotUIState> = _state

    private val _username: MutableStateFlow<String> = MutableStateFlow("")

    init {
        getSpotAttributesList()
        getUserInfo()
    }

    fun onEvent(event: AddSpotUIEvent) {
        when (event) {
            is AddSpotUIEvent.ImageSelected -> {
                addSelectedImage(event.uri)
            }

            is AddSpotUIEvent.AttributeClicked -> {
                modifySelectedAttributes(event.attribute)
            }

            AddSpotUIEvent.ClearErrorToastText -> {
                clearErrorToastText()
            }

            AddSpotUIEvent.ClearUploadProgress -> {
                clearUploadProgressState()
            }

            AddSpotUIEvent.CreateNewSpot -> {
                createNewSpotIntent()
            }

            AddSpotUIEvent.DeleteSelectedImage -> {
                removeSelectedImageFromList()
            }

            AddSpotUIEvent.ObtainCountryAndLocality -> {
                obtainCountryAndCityFromLatLng()
            }

            is AddSpotUIEvent.ShowExitDialog -> {
                setShowExitDialog(event.state)
            }

            is AddSpotUIEvent.UpdateSpotDescription -> {
                modifySpotDescription(event.text)
            }

            is AddSpotUIEvent.UpdateSpotLocation -> {
                modifySpotLocation(event.location)
            }

            is AddSpotUIEvent.UpdateSpotName -> {
                modifySpotTitle(event.text)
            }

            is AddSpotUIEvent.UpdateSpotScore -> {
                modifyReviewScore(event.score)
            }

            is AddSpotUIEvent.UpdateSelectedImages -> {
                updateSelectedImages(event.uris)
            }
        }
    }

    private fun getSpotAttributesList() {
        viewModelScope.launch {
            val result = getSpotAttributesUseCase.invoke()
            if (result.isNotEmpty()) {
                _state.value = _state.value.copy(attributesList = result)
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
        _state.value = _state.value.copy(spotScore = score.roundToInt())
    }

    private fun modifySelectedAttributes(attribute: SpotAttribute) {
        if (!_state.value.selectedAttributes.contains(attribute)) {
            _state.value =
                _state.value.copy(selectedAttributes = _state.value.selectedAttributes + attribute)
        } else {
            _state.value =
                _state.value.copy(selectedAttributes = _state.value.selectedAttributes - attribute)
        }
    }

    private fun updateSelectedImages(uris: List<Uri>) {
        if (
            uris.size <= MAX_IMAGES_SELECTED &&
            state.value.imageUris.size <= MAX_IMAGES_SELECTED &&
            _state.value.imageUris.size + uris.size <= MAX_IMAGES_SELECTED
        ) {
            _state.value = _state.value.copy(imageUris = _state.value.imageUris + uris)
        } else {
            _state.value = _state.value.copy(errorToastText = R.string.max_images_toast)
        }
    }

    private fun removeSelectedImageFromList() {
        _state.value =
            _state.value.copy(imageUris = _state.value.imageUris.filterNot { it == _state.value.selectedImageUri })
        _state.value = _state.value.copy(selectedImageUri = Uri.EMPTY)
    }

    private fun addSelectedImage(uri: Uri) {
        if (_state.value.selectedImageUri == uri) {
            _state.value = _state.value.copy(selectedImageUri = Uri.EMPTY)
        } else {
            _state.value = _state.value.copy(selectedImageUri = uri)
        }
    }

    private fun modifySpotTitle(title: String) {
        _state.value = _state.value.copy(spotName = title)
    }

    private fun modifySpotDescription(description: String) {
        _state.value = _state.value.copy(spotDescription = description)
    }

    private fun modifySpotLocation(location: LatLng) {
        _state.value = _state.value.copy(spotLocation = location)
    }

    private fun clearUploadProgressState() {
        _state.value = _state.value.copy(uploadProgress = Resource.Success(""))
    }

    private fun clearErrorToastText() {
        _state.value = _state.value.copy(errorToastText = -1)
    }

    private fun setShowExitDialog(state: Boolean) {
        _state.value = _state.value.copy(showExitDialog = state)
    }

    private fun obtainCountryAndCityFromLatLng() {
        viewModelScope.launch {
            locationRepository.obtainCountryFromLatLng(_state.value.spotLocation).collectLatest {
                _state.value = _state.value.copy(spotLocationCountry = it)
            }
            locationRepository.obtainLocalityFromLatLng(_state.value.spotLocation).collectLatest {
                _state.value = _state.value.copy(spotLocationLocality = it)
            }
        }
    }

    private fun createNewSpotIntent() {
        viewModelScope.launch {
            databaseRepository.createSpot(
                featuredImages = _state.value.imageUris,
                spotTitle = _state.value.spotName,
                spotDescription = _state.value.spotDescription,
                spotLocation = _state.value.spotLocation,
                spotCountry = _state.value.spotLocationCountry,
                spotLocality = _state.value.spotLocationLocality,
                spotAuthor = _username.value,
                spotAttributes = _state.value.selectedAttributes,
                spotScore = _state.value.spotScore
            ).collectLatest {
                _state.value = _state.value.copy(uploadProgress = it)
            }
        }
    }

}