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

    private val _selectedImageUris: MutableStateFlow<List<Uri>> = MutableStateFlow(listOf())
    val selectedImageUris: StateFlow<List<Uri>> = _selectedImageUris

    fun updateSelectedImages(uris: List<Uri>) {
        _selectedImageUris.value = uris
    }

}