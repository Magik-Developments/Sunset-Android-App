package com.madteam.sunset.ui.screens.welcome.state

import com.google.firebase.auth.AuthResult
import com.madteam.sunset.utils.Resource

data class WelcomeUIState(
    val signInState: Resource<AuthResult?> = Resource.Success(null),
    val isLoading: Boolean = false
)