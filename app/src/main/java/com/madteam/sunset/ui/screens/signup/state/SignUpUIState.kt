package com.madteam.sunset.ui.screens.signup.state

import com.google.firebase.auth.AuthResult
import com.madteam.sunset.utils.Resource

data class SignUpUIState(
    val signUpState: Resource<AuthResult?> = Resource.Success(null),
    val isValidForm: Boolean = false,
    val email: String = "",
    val password: String = "",
    val username: String = "",
    val isEmailValid: Boolean = false,
    val isUsernameValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val showDialog: Boolean = false,
    val emailAlreadyInUse: Boolean = false,
    val usernameAlreadyInUse: Boolean = false
)