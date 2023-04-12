package com.madteam.sunset.welcome.ui.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

  private val _email = MutableStateFlow("")
  val email: StateFlow<String> = _email

  private val _password = MutableStateFlow("")
  val password: StateFlow<String> = _password

  private val _formError = MutableStateFlow(false)
  val formError: StateFlow<Boolean> = _formError

  fun onValuesSignInChange(emailValue: String, passwordValue: String) {
    _email.value = emailValue
    _password.value = passwordValue
    checkIfFormIsValid()
  }

  private fun checkIfEmailIsValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
  }

  fun checkIfFormIsValid() {
    _formError.value = (checkIfEmailIsValid() && _password.value.isNotBlank())
  }
  
}