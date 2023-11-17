package com.madteam.sunset.ui.screens.verifyaccount.state

sealed class VerifyAccountUIEvent {
    data object OnResend : VerifyAccountUIEvent()
    data class OnCheck(val credential: String) : VerifyAccountUIEvent()
}