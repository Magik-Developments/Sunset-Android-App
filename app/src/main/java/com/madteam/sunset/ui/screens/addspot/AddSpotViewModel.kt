package com.madteam.sunset.ui.screens.addspot

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.repositories.LocationRepository
import com.madteam.sunset.ui.screens.addpost.MAX_IMAGES_SELECTED
import com.madteam.sunset.utils.googlemaps.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSpotViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _imageUris: MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    private val _selectedImageUri: MutableStateFlow<Uri> = MutableStateFlow(Uri.EMPTY)
    val selectedImageUri: StateFlow<Uri> = _selectedImageUri

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

    fun updateSelectedImages(uris: List<Uri>) {
        if (uris.size <= MAX_IMAGES_SELECTED && _imageUris.value.size <= MAX_IMAGES_SELECTED && _imageUris.value.size + uris.size <= MAX_IMAGES_SELECTED) {
            _imageUris.value = _imageUris.value + uris
        } else {
            //TODO: Error text _errorToastText.value = "Maximum 8 images"
        }
    }

    fun removeSelectedImageFromList() {
        _imageUris.value = imageUris.value.filterNot { it == _selectedImageUri.value }
        _selectedImageUri.value = Uri.EMPTY
    }

    fun addSelectedImage(uri: Uri) {
        if (_selectedImageUri.value == uri) {
            _selectedImageUri.value = Uri.EMPTY
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

}