package com.madteam.sunset.ui.screens.verifyaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.repositories.AuthContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyAccountViewModel @Inject constructor(
  private val authRepository: AuthContract
) : ViewModel() {

  val resendCounter = MutableStateFlow(120)

  init {
    sendVerifyEmailIntent()
    startResendCountDown()
  }

  private fun startResendCountDown() = viewModelScope.launch {
    while (resendCounter.value > 0) {
      delay(1000)
      resendCounter.value--
    }
  }

  fun sendVerifyEmailIntent() = viewModelScope.launch {
    authRepository.sendVerifyEmailIntent()
  }
}