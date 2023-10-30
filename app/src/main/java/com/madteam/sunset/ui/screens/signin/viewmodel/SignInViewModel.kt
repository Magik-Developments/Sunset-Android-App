package com.madteam.sunset.ui.screens.signin.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.repositories.AuthContract
import com.madteam.sunset.ui.screens.signin.state.SignInUIEvent
import com.madteam.sunset.ui.screens.signin.state.SignInUIState
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthContract
) : ViewModel() {

    private val _state: MutableStateFlow<SignInUIState> = MutableStateFlow(SignInUIState())
    val state: StateFlow<SignInUIState> = _state

    fun onEvent(event: SignInUIEvent) {
        when (event) {
            is SignInUIEvent.ClearSignInState -> {
                clearSignInState()
            }

            is SignInUIEvent.SignInIntent -> {
                if (_state.value.isValidForm) {
                    signInWithEmailAndPasswordIntent()
                }
            }

            is SignInUIEvent.UpdateEmail -> {
                _state.value = _state.value.copy(email = event.email)
                validateForm()
                _state.value = _state.value.copy(invalidCredentials = false)
            }

            is SignInUIEvent.UpdatePassword -> {
                _state.value = _state.value.copy(password = event.password)
                validateForm()
                _state.value = _state.value.copy(invalidCredentials = false)
            }
        }
    }

    private fun isPasswordValid(): Boolean {
        return _state.value.password.isNotBlank() && _state.value.password.length >= 6
    }

    private fun validateForm() {
        _state.value = _state.value.copy(
            isValidEmail = Patterns.EMAIL_ADDRESS.matcher(_state.value.email).matches()
        )
        _state.value =
            _state.value.copy(isValidForm = (_state.value.isValidEmail && isPasswordValid()))
    }

    private fun signInWithEmailAndPasswordIntent() {
        viewModelScope.launch {
            authRepository.doSignInWithPasswordAndEmail(_state.value.email, _state.value.password)
                .collectLatest {
                    _state.value = _state.value.copy(signInState = it)
                    when (it) {
                        is Resource.Error -> {
                            _state.value = _state.value.copy(invalidCredentials = true)
                        }

                        else -> {
                            //Not necessary
                        }
                    }
                }
        }
    }

    private fun clearSignInState() {
        _state.value = _state.value.copy(signInState = Resource.Success(null))
    }
}