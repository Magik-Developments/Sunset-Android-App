package com.madteam.sunset.ui.screens.lostpassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.repositories.AuthContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LostPasswordViewModel @Inject constructor(
  private val authRepository: AuthContract
) : ViewModel() {

  val isValidForm = MutableStateFlow(false)

  fun validateForm(email: String) {
    isValidForm.value = isEmailValid(email)
  }

  private fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
  }

  fun resetPasswordWithEmailIntent(email: String) = viewModelScope.launch {
    authRepository.resetPasswordWithEmailIntent(email)
  }
}