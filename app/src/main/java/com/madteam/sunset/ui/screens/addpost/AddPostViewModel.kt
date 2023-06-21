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

    fun updateSelectedImages(uris: List<Uri>) {
        _imageUris.value = _imageUris.value + uris
    }

    fun removeSelectedImageFromList(uri: Uri) {
        _imageUris.value = imageUris.value.filterNot { it == uri }
    }

    fun addSelectedImage(uri: Uri) {
        _selectedImageUri.value = uri
    }

}