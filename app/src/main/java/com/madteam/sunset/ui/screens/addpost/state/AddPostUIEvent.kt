package com.madteam.sunset.ui.screens.addpost.state

import android.net.Uri

sealed class AddPostUIEvent {
    data class ShowExitDialog(val state: Boolean) : AddPostUIEvent()
    data class AddSelectedImage(val uri: Uri) : AddPostUIEvent()
    data class UpdateDescriptionText(val text: String) : AddPostUIEvent()
    data class UpdateSelectedImages(val uris: List<Uri>) : AddPostUIEvent()
    data class CreateNewPost(val spotReference: String) : AddPostUIEvent()
    data object RemoveSelectedImage : AddPostUIEvent()
    data object ClearErrorToastText : AddPostUIEvent()
    data object ClearUploadProgress : AddPostUIEvent()
}