package com.madteam.sunset.ui.screens.verifyaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.utils.Resource.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
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

  val resendCounter = MutableStateFlow(0)
  val userVerified = MutableStateFlow(false)
  val recheckCounter = MutableStateFlow(0)

  init {
    sendVerifyEmailIntent()
    startResendCountDown()
  }

  private fun startResendCountDown() = viewModelScope.launch {
    if (resendCounter.value <= 0) {
      resendCounter.value = RESEND_TIME
      while (resendCounter.value > 0) {
        delay(1000)
        resendCounter.value--
      }
    }
  }

  private fun startRecheckCountDown() = viewModelScope.launch {
    while (recheckCounter.value > 0) {
      delay(1000)
      recheckCounter.value--
    }
  }

  fun sendVerifyEmailIntent() = viewModelScope.launch {
    authRepository.sendVerifyEmailIntent()
  }

  fun checkIfUserIsVerified(credential: String) = viewModelScope.launch {
    authRepository.reauthenticateUser(credential).collect()
    authRepository.checkIfUserEmailIsVerified(credential).collectLatest { result ->
      when (result) {
        is Success -> {
          userVerified.value = result.data!!
        }

        else -> { /* Not necessary */ }
      }
    }
    if (!userVerified.value) {
      recheckCounter.value = RECHECK_TIME
      startRecheckCountDown()
    }
  }
}