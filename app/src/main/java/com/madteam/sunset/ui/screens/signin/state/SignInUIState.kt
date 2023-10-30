package com.madteam.sunset.ui.screens.signin.state

import com.google.firebase.auth.AuthResult
import com.madteam.sunset.utils.Resource

data class SignInUIState(
    val signInState: Resource<AuthResult?> = Resource.Success(null),
    val isValidForm: Boolean = false,
    val isValidEmail: Boolean = false,
    val email: String = "",
    val password: String = "",
    val invalidCredentials: Boolean = false
)