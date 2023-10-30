package com.madteam.sunset.ui.screens.lostpassword.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.repositories.AuthContract
import com.madteam.sunset.ui.screens.lostpassword.state.LostPasswordUIEvent
import com.madteam.sunset.ui.screens.lostpassword.state.LostPasswordUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LostPasswordViewModel @Inject constructor(
    private val authRepository: AuthContract
) : ViewModel() {

    private val _state: MutableStateFlow<LostPasswordUIState> =
        MutableStateFlow(LostPasswordUIState())
    val state: StateFlow<LostPasswordUIState> = _state

    fun onEvent(event: LostPasswordUIEvent) {
        when (event) {
            is LostPasswordUIEvent.ValidateForm -> {
                validateForm(event.email)
            }

            is LostPasswordUIEvent.ResetPasswordWithEmailIntent -> {
                resetPasswordWithEmailIntent(event.email)
            }
        }
    }

    private fun validateForm(email: String) {
        _state.value = _state.value.copy(isValidForm = isEmailValid(email))
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun resetPasswordWithEmailIntent(email: String) = viewModelScope.launch {
        authRepository.resetPasswordWithEmailIntent(email)
    }
}