package com.madteam.sunset.ui.screens.editprofile.state

import com.madteam.sunset.utils.Resource

data class EditProfileUIState(
    val username: String = "",
    val email: String = "",
    val name: String = "",
    val location: String = "",
    val userImage: String = "",
    val userIsAdmin: Boolean = false,
    val dataHasChanged: Boolean = false,
    val uploadProgress: Resource<String> = Resource.Success(""),
)
