package com.madteam.sunset.ui.screens.editprofile.state

import android.net.Uri

sealed class EditProfileUIEvent {
    data class UpdateName(val name: String) : EditProfileUIEvent()
    data class UpdateLocation(val location: String) : EditProfileUIEvent()
    data class UpdateProfileImage(val uri: Uri) : EditProfileUIEvent()
    data object SaveData : EditProfileUIEvent()
    data object ClearUploadProgress : EditProfileUIEvent()

}