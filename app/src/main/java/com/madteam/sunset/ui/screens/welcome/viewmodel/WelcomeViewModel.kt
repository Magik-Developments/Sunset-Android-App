package com.madteam.sunset.ui.screens.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.madteam.sunset.data.repositories.AuthRepository
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
    val googleSignInClient: GoogleSignInClient,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state: MutableStateFlow<WelcomeUIState> = MutableStateFlow(WelcomeUIState())
    val state: StateFlow<WelcomeUIState> = _state

    fun onEvent(event: WelcomeUIEvent) {
        when (event) {
            is WelcomeUIEvent.HandleGoogleSignInResult -> handleGoogleSignInResult(event.result)
            is WelcomeUIEvent.ClearSignInState -> clearSignInState()
            is WelcomeUIEvent.ShowLoading -> {
                _state.value = _state.value.copy(
                    isLoading = event.show
                )
            }
            is WelcomeUIEvent.SignInAsGuest -> signInAsGuest()
        }
    }

    private fun signInAsGuest() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            authRepository.doSignInAsGuest().collectLatest {
                when (it) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            signInState = Resource.Error(it.message!!)
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            signInState = Resource.Loading()
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            signInState = Resource.Success(it.data)
                        )
                    }
                }
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }

    }

    private fun handleGoogleSignInResult(result: GoogleSignInAccount) {
        viewModelScope.launch {
            signInWithGoogleUseCase(result.idToken ?: "").collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        if (result.message!! == "e_google_user_first_time") {
                            _state.value = _state.value.copy(
                                signInState = Resource.Error("e_google_user_first_time")
                            )
                        } else {
                            _state.value = _state.value.copy(
                                signInState = Resource.Error("Error, try again later")
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            signInState = Resource.Loading()
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            signInState = Resource.Success(result.data)
                        )
                    }
                }
                _state.value = _state.value.copy(
                    isLoading = false
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
