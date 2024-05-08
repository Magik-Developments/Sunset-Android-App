package com.madteam.sunset.ui.screens.welcome.state

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed class WelcomeUIEvent {
    data class HandleGoogleSignInResult(val result: GoogleSignInAccount) : WelcomeUIEvent()
    data object ClearSignInState : WelcomeUIEvent()
    data object SignInAsGuest : WelcomeUIEvent()
    data class ShowLoading(val show: Boolean) : WelcomeUIEvent()
}
