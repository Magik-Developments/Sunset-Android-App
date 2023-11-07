package com.madteam.sunset.ui.screens.enterusername.state

import com.madteam.sunset.utils.Resource

data class EnterUsernameUIState(
    val username: String = "",
    val usernameIsValid: Boolean = false,
    val formEnabled: Boolean = true,
    val showDialog: Boolean = false,
    val signUpState: Resource<String> = Resource.Success("")
)