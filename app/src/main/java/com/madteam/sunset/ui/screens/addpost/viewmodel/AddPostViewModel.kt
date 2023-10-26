package com.madteam.sunset.ui.screens.addpost.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.R
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent.AddSelectedImage
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent.ClearErrorToastText
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent.ClearUploadProgress
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent.CreateNewPost
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent.RemoveSelectedImage
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent.ShowExitDialog
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent.UpdateDescriptionText
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIEvent.UpdateSelectedImages
import com.madteam.sunset.ui.screens.addpost.state.AddPostUIState
import com.madteam.sunset.ui.screens.addpost.ui.MAX_IMAGES_SELECTED
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private lateinit var username: String

    private val _state = MutableStateFlow(AddPostUIState())
    val state: StateFlow<AddPostUIState> = _state

    init {
        getUserInfo()
    }

    fun onEvent(event: AddPostUIEvent) {
        when (event) {
            is ClearErrorToastText -> {
                clearErrorToastText()
            }

            is AddSelectedImage -> {
                addSelectedImage(event.uri)
            }

            is RemoveSelectedImage -> {
                removeSelectedImageFromList()
            }

            is UpdateDescriptionText -> {
                updateDescriptionText(event.text)
            }

            is ShowExitDialog -> {
                setShowExitDialog(event.state)
            }

            is ClearUploadProgress -> {
                clearUpdateProgressState()
            }

            is UpdateSelectedImages -> {
                updateSelectedImages(event.uris)
            }

            is CreateNewPost -> {
                createNewPost(event.spotReference)
            }
        }
    }

    private fun updateSelectedImages(uris: List<Uri>) {
        if (
            uris.size <= MAX_IMAGES_SELECTED &&
            _state.value.imageUris.size <= MAX_IMAGES_SELECTED &&
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
        if (_state.value.imageUris.size <= MAX_IMAGES_SELECTED) {
            _state.value = _state.value.copy(imageUris = _state.value.imageUris + uri)
        } else {
            _state.value = _state.value.copy(errorToastText = R.string.max_images_toast)
        }
    }

    private fun updateDescriptionText(text: String) {
        _state.value = _state.value.copy(descriptionText = text)
    }

    private fun setShowExitDialog(state: Boolean) {
        _state.value = _state.value.copy(showExitDialog = state)
    }

    private fun clearErrorToastText() {
        _state.value = _state.value.copy(errorToastText = -1)
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                username = databaseRepository.getUserByEmail(user.email!!).username
            }
        }
    }

    private fun createNewPost(spotRef: String) {
        viewModelScope.launch {
            databaseRepository.createSpotPost(
                spotRef = spotRef,
                description = _state.value.descriptionText,
                imagesUriList = _state.value.imageUris,
                authorUsername = username
            ).collectLatest {
                _state.value = _state.value.copy(uploadProgress = it)
            }
        }
    }

    private fun clearUpdateProgressState() {
        _state.value = _state.value.copy(uploadProgress = Resource.Success(""))
    }

}