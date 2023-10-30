package com.madteam.sunset.ui.screens.verifyaccount.state

data class VerifyAccountUIState(
    val resendCounter: Int = 0,
    val recheckCounter: Int = 0,
    val isVerified: Boolean = false
)