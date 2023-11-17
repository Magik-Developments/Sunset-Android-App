package com.madteam.sunset.ui.screens.addpost.state

import android.net.Uri
import androidx.annotation.StringRes
import com.madteam.sunset.utils.Resource

data class AddPostUIState(
    val selectedImageUri: Uri = Uri.EMPTY,
    val imageUris: List<Uri> = listOf(),
    val descriptionText: String = "",
    val showExitDialog: Boolean = false,
    @StringRes val errorToastText: Int = -1,
    val uploadProgress: Resource<String> = Resource.Success("")
)