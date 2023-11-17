package com.madteam.sunset.ui.screens.verifyaccount.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.repositories.AuthContract
import com.madteam.sunset.ui.screens.verifyaccount.state.VerifyAccountUIEvent
import com.madteam.sunset.ui.screens.verifyaccount.state.VerifyAccountUIState
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RESEND_TIME = 60
private const val RECHECK_TIME = 10

@HiltViewModel
class VerifyAccountViewModel @Inject constructor(
    private val authRepository: AuthContract
) : ViewModel() {

    private val _state: MutableStateFlow<VerifyAccountUIState> = MutableStateFlow(
        VerifyAccountUIState()
    )
    val state: StateFlow<VerifyAccountUIState> = _state

    init {
        sendVerifyEmailIntent()
        startResendCountDown()
    }

    fun onEvent(event: VerifyAccountUIEvent) {
        when (event) {
            is VerifyAccountUIEvent.OnResend -> {
                sendVerifyEmailIntent()
            }

            is VerifyAccountUIEvent.OnCheck -> {
                checkIfUserIsVerified(event.credential)
            }
        }

    }

    private fun resetResendCounter() {
        _state.value = _state.value.copy(resendCounter = RESEND_TIME)
    }

    private fun resetRecheckCounter() {
        _state.value = _state.value.copy(recheckCounter = RECHECK_TIME)
    }

    private fun startResendCountDown() = viewModelScope.launch {
        if (_state.value.resendCounter <= 0) {
            resetResendCounter()
            while (_state.value.resendCounter > 0) {
                delay(1000)
                _state.value = _state.value.copy(resendCounter = _state.value.resendCounter - 1)
            }
        }
    }

    private fun startRecheckCountDown() = viewModelScope.launch {
        while (_state.value.recheckCounter > 0) {
            delay(1000)
            _state.value = _state.value.copy(recheckCounter = _state.value.recheckCounter - 1)
        }
    }

    private fun sendVerifyEmailIntent() {
        viewModelScope.launch {
            authRepository.sendVerifyEmailIntent()
            startResendCountDown()
        }
    }

    private fun checkIfUserIsVerified(credential: String) {
        viewModelScope.launch {
            authRepository.reauthenticateUser(credential).collect()
            authRepository.checkIfUserEmailIsVerified(credential).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(isVerified = result.data!!)
                    }

                    else -> {
                        /* Not necessary */
                    }
                }
            }
            if (!_state.value.isVerified) {
                resetRecheckCounter()
                startRecheckCountDown()
            }
        }
    }
}