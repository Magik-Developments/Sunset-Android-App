package com.madteam.sunset.welcome.ui.signup

data class SingUpState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
