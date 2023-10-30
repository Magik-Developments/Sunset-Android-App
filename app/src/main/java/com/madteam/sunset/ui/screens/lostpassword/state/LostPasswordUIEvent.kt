package com.madteam.sunset.ui.screens.lostpassword.state

sealed class LostPasswordUIEvent {
    data class ValidateForm(val email: String) : LostPasswordUIEvent()
    data class ResetPasswordWithEmailIntent(val email: String) : LostPasswordUIEvent()

}