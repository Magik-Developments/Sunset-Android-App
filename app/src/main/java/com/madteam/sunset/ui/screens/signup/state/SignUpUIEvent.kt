package com.madteam.sunset.ui.screens.signup.state

sealed class SignUpUIEvent {
    data class SetShowDialog(val showDialog: Boolean) : SignUpUIEvent()
    data class UpdateEmail(val email: String) : SignUpUIEvent()
    data class UpdatePassword(val password: String) : SignUpUIEvent()
    data class UpdateUsername(val username: String) : SignUpUIEvent()
    data object SignUpIntent : SignUpUIEvent()
    data object ClearSignUpState : SignUpUIEvent()
}