package com.madteam.sunset.ui.screens.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.madteam.sunset.domain.usecases.SignInWithGoogleUseCase
import com.madteam.sunset.ui.screens.welcome.state.WelcomeUIEvent
import com.madteam.sunset.ui.screens.welcome.state.WelcomeUIState
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _state: MutableStateFlow<WelcomeUIState> = MutableStateFlow(WelcomeUIState())
    val state: StateFlow<WelcomeUIState> = _state

    fun onEvent(event: WelcomeUIEvent) {
        when (event) {
            is WelcomeUIEvent.HandleGoogleSignInResult -> handleGoogleSignInResult(event.result)
            is WelcomeUIEvent.ClearSignInState -> clearSignInState()
        }
    }

    private fun handleGoogleSignInResult(result: GoogleSignInAccount) {
        viewModelScope.launch {
            signInWithGoogleUseCase(result.idToken ?: "").collectLatest { result ->
                _state.value = _state.value.copy(
                    signInState = Resource.Success(result.data)
                )
            }
        }
    }

    private fun clearSignInState() {
        _state.value = _state.value.copy(
            signInState = Resource.Success(null)
        )
    }

}
