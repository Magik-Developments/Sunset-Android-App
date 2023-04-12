package com.madteam.sunset.welcome.ui.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.nulabinc.zxcvbn.Zxcvbn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

  private val _email = MutableStateFlow("")
  val email: StateFlow<String> = _email

  private val _password = MutableStateFlow("")
  val password: StateFlow<String> = _password

  private val _username = MutableStateFlow("")
  val username: StateFlow<String> = _username

  private val _formError = MutableStateFlow(false)
  val formError: StateFlow<Boolean> = _formError

  fun onValuesSignInChange(emailValue: String, passwordValue: String, usernameValue: String) {
    _email.value = emailValue
    _password.value = passwordValue
    _username.value = usernameValue
    checkIfFormIsValid()
    checkPasswordStrength()
  }

  private fun checkIfEmailIsValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
  }

  private fun checkPasswordStrength(): Int {
    return Zxcvbn().measure(_password.value).score
  }

  private fun checkIfFormIsValid() {
    _formError.value = (checkIfEmailIsValid() && _password.value.isNotBlank())
  }
  
}