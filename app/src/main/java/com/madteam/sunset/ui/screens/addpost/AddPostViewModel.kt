package com.madteam.sunset.ui.screens.addpost

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(

) : ViewModel() {

    private val _selectedImageUri: MutableStateFlow<Uri> = MutableStateFlow(Uri.EMPTY)
    val selectedImageUri: StateFlow<Uri> = _selectedImageUri

    private val _imageUris: MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    private val _descriptionText: MutableStateFlow<String> = MutableStateFlow("")
    val descriptionText: StateFlow<String> = _descriptionText

    private val _showExitDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showExitDialog: StateFlow<Boolean> = _showExitDialog

    private val _errorToastText: MutableStateFlow<String> = MutableStateFlow("")
    val errorToastText: StateFlow<String> = _errorToastText

    fun updateSelectedImages(uris: List<Uri>) {
        if (uris.size <= MAX_IMAGES_SELECTED && _imageUris.value.size <= MAX_IMAGES_SELECTED && _imageUris.value.size + uris.size <= MAX_IMAGES_SELECTED) {
            _imageUris.value = _imageUris.value + uris
        } else {
            _errorToastText.value = "Maximum 8 images"
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

    fun updateDescriptionText(text: String) {
        _descriptionText.value = text
    }

    fun setShowExitDialog(state: Boolean) {
        _showExitDialog.value = state
    }

    fun clearErrorToastText() {
        _errorToastText.value = ""
    }

}