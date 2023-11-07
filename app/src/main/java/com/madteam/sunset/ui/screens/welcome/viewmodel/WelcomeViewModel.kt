package com.madteam.sunset.ui.screens.welcome.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.madteam.sunset.ui.screens.welcome.state.WelcomeUIEvent
import com.madteam.sunset.ui.screens.welcome.state.WelcomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _state: MutableStateFlow<WelcomeUIState> = MutableStateFlow(WelcomeUIState())
    val state: StateFlow<WelcomeUIState> = _state

    fun onEvent(event: WelcomeUIEvent) {
        when (event) {
            is WelcomeUIEvent.HandleGoogleSignInResult -> handleGoogleSignInResult(event.result)
        }
    }

    private fun handleGoogleSignInResult(result: GoogleSignInAccount) {
        TODO("Not yet implemented")
    }

}
