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

  private val _passwordStrength = MutableStateFlow(0)
  val passwordStrength: StateFlow<Int> = _passwordStrength

  private val _validEmail = MutableStateFlow(false)
  val validEmail: StateFlow<Boolean> = _validEmail

  private val _validUsername = MutableStateFlow(false)
  val validUsername: StateFlow<Boolean> = _validUsername

  private val _showDialog = MutableStateFlow(false)
  val showDialog: StateFlow<Boolean> = _showDialog

  fun onValuesSignUpChange(emailValue: String, passwordValue: String, usernameValue: String) {
    _email.value = emailValue
    _password.value = passwordValue
    _username.value = usernameValue
    checkIfFormIsValid()
    checkPasswordStrength()
  }

  private fun checkIfEmailIsValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    //todo: validar con firebase si el mail ya está registrado o no
  }

  private fun checkIfUsernameIsValid(): Boolean {
    return (_username.value.length > 5)
    // TODO: validar con firebase si el usuario ya existe o no
  }

  private fun checkPasswordStrength() {
    _passwordStrength.value = Zxcvbn().measure(_password.value).score
  }

  private fun checkIfFormIsValid() {
    _formError.value = (checkIfEmailIsValid() && _passwordStrength.value > 0 && checkIfUsernameIsValid())
    _validEmail.value = checkIfEmailIsValid()
    _validUsername.value = checkIfUsernameIsValid()
  }

  fun goToPoliciesScreen() {
    _showDialog.value = false
  }

  fun signUpIntent() {
    _showDialog.value = false
  }

  fun showPrivacyDialog() {
    _showDialog.value = true
  }
}