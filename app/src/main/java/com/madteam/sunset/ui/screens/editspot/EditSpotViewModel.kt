package com.madteam.sunset.ui.screens.editspot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.repositories.AuthRepository
import com.madteam.sunset.repositories.DatabaseRepository
import com.madteam.sunset.repositories.LocationRepository
import com.madteam.sunset.ui.screens.addpost.MAX_IMAGES_SELECTED
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.googlemaps.MapState
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
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _imageUris: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val imageUris: StateFlow<List<String>> = _imageUris

    private val _selectedImageUri: MutableStateFlow<String> = MutableStateFlow("")
    val selectedImageUri: StateFlow<String> = _selectedImageUri

    private val _spotTitle: MutableStateFlow<String> = MutableStateFlow("")
    val spotTitle: StateFlow<String> = _spotTitle

    private val _spotDescription: MutableStateFlow<String> = MutableStateFlow("")
    val spotDescription: StateFlow<String> = _spotDescription

    private val _spotLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val spotLocation: StateFlow<LatLng> = _spotLocation

    private val _mapState: MutableStateFlow<MapState> = MutableStateFlow(MapState())
    val mapState: StateFlow<MapState> = _mapState

    private val _spotLocationLocality: MutableStateFlow<String> = MutableStateFlow("")
    val spotLocationLocality: StateFlow<String> = _spotLocationLocality

    private val _spotLocationCountry: MutableStateFlow<String> = MutableStateFlow("")
    val spotLocationCountry: StateFlow<String> = _spotLocationCountry

    private val _attributesList: MutableStateFlow<List<SpotAttribute>> =
        MutableStateFlow(mutableListOf())
    val attributesList: StateFlow<List<SpotAttribute>> = _attributesList

    private val _selectedAttributes: MutableStateFlow<List<SpotAttribute>> =
        MutableStateFlow(mutableListOf())
    val selectedAttributes: StateFlow<List<SpotAttribute>> = _selectedAttributes

    private val _spotScore: MutableStateFlow<Int> =
        MutableStateFlow(0)
    val spotScore: StateFlow<Int> = _spotScore

    private val _showExitDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showExitDialog: StateFlow<Boolean> = _showExitDialog

    private val _errorToastText: MutableStateFlow<String> = MutableStateFlow("")
    val errorToastText: StateFlow<String> = _errorToastText

    private val _uploadProgress: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Success(""))
    val uploadProgress: StateFlow<Resource<String>> = _uploadProgress

    private val _spotReference: MutableStateFlow<String> = MutableStateFlow("")

    private val _username: MutableStateFlow<String> = MutableStateFlow("")

    init {
        getSpotAttributesList()
        getUserInfo()
    }

    fun setSpotReference(docReference: String) {
        _spotReference.value = docReference
        getSpotInfo()
    }

    private fun getSpotInfo() {
        viewModelScope.launch {
            databaseRepository.getSpotByDocRef(_spotReference.value).collectLatest { spot ->
                _spotTitle.value = spot.name
                _imageUris.value = spot.featuredImages
                _spotDescription.value = spot.description
                _spotScore.value = spot.score.roundToInt()
                _selectedAttributes.value = spot.attributes
                _spotLocation.value =
                    LatLng(spot.locationInLatLng.latitude, spot.locationInLatLng.longitude)
            }
        }
    }

    private fun getSpotAttributesList() {
        viewModelScope.launch {
            databaseRepository.getAllSpotAttributes().collectLatest { attributesList ->
                _attributesList.value = attributesList
            }
        }
    }

    private fun getUserInfo() {
        authRepository.getCurrentUser()?.let { user ->
            databaseRepository.getUserByEmail(user.email!!) {
                _username.value = it.username
            }
        }
    }

    fun modifyReviewScore(score: Float) {
        _spotScore.value = score.roundToInt()
    }

    fun modifySelectedAttributes(attribute: SpotAttribute) {
        if (!_selectedAttributes.value.contains(attribute)) {
            _selectedAttributes.value = _selectedAttributes.value + attribute
        } else {
            _selectedAttributes.value = _selectedAttributes.value - attribute
        }
    }

    fun updateSelectedImages(uris: List<String>) {
        if (uris.size <= MAX_IMAGES_SELECTED && _imageUris.value.size <= MAX_IMAGES_SELECTED && _imageUris.value.size + uris.size <= MAX_IMAGES_SELECTED) {
            _imageUris.value = _imageUris.value + uris
        } else {
            //TODO: Error text _errorToastText.value = "Maximum 8 images"
        }
    }

    fun removeSelectedImageFromList() {
        _imageUris.value = imageUris.value.filterNot { it == _selectedImageUri.value }
        _selectedImageUri.value = ""
    }

    fun addSelectedImage(uri: String) {
        if (_selectedImageUri.value == uri) {
            _selectedImageUri.value = ""
        } else {
            _selectedImageUri.value = uri
        }
    }

    fun modifySpotTitle(title: String) {
        _spotTitle.value = title
    }

    fun modifySpotDescription(description: String) {
        _spotDescription.value = description
    }

    fun modifySpotLocation(location: LatLng) {
        _spotLocation.value = location
    }

    fun clearUploadProgressState() {
        _uploadProgress.value = Resource.Success("")
    }

    fun clearErrorToastText() {
        _errorToastText.value = ""
    }

    fun setShowExitDialog(state: Boolean) {
        _showExitDialog.value = state
    }

    fun obtainCountryAndCityFromLatLng() {
        viewModelScope.launch {
            locationRepository.obtainCountryFromLatLng(_spotLocation.value).collectLatest {
                _spotLocationCountry.value = it
            }
            locationRepository.obtainLocalityFromLatLng(_spotLocation.value).collectLatest {
                _spotLocationLocality.value = it
            }
        }
    }

    fun createNewSpotIntent() {
        viewModelScope.launch {
            databaseRepository.createSpot(
                featuredImages = listOf(),
                spotTitle = _spotTitle.value,
                spotDescription = _spotDescription.value,
                spotLocation = _spotLocation.value,
                spotCountry = _spotLocationCountry.value,
                spotLocality = _spotLocationLocality.value,
                spotAuthor = _username.value,
                spotAttributes = _selectedAttributes.value,
                spotScore = _spotScore.value
            ).collectLatest {
                _uploadProgress.value = it
            }
        }
    }

}