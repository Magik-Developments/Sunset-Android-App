package com.madteam.sunset.ui.screens.signin.state

sealed class SignInUIEvent {
    data class UpdateEmail(val email: String) : SignInUIEvent()
    data class UpdatePassword(val password: String) : SignInUIEvent()
    data object SignInIntent : SignInUIEvent()
    data object ClearSignInState : SignInUIEvent()

}