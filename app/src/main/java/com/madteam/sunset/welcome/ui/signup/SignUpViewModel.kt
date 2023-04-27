package com.madteam.sunset.welcome.ui.signup

import android.os.Build.VERSION_CODES
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.common.utils.Resource.Error
import com.madteam.sunset.common.utils.Resource.Loading
import com.madteam.sunset.common.utils.Resource.Success
import com.madteam.sunset.welcome.domain.interactor.FirebaseAuthInteractor
import com.madteam.sunset.welcome.domain.interactor.FirebaseFirestoreInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
  private val firebaseAuthInteractor: FirebaseAuthInteractor,
  private val firebaseFirestoreInteractor: FirebaseFirestoreInteractor
) :
  ViewModel() {

  private val _email = MutableStateFlow("")
  val email: StateFlow<String> = _email

  private val _password = MutableStateFlow("")
  val password: StateFlow<String> = _password

  private val _username = MutableStateFlow("")
  val username: StateFlow<String> = _username

  private val _formError = MutableStateFlow(false)
  val formError: StateFlow<Boolean> = _formError

  private val _validEmail = MutableStateFlow(false)
  val validEmail: StateFlow<Boolean> = _validEmail

  private val _validUsername = MutableStateFlow(false)
  val validUsername: StateFlow<Boolean> = _validUsername

  private val _showDialog = MutableStateFlow(false)
  val showDialog: StateFlow<Boolean> = _showDialog

  private val _signUpState = Channel<SignUpState>()
  val signUpState = _signUpState.receiveAsFlow()

  fun onValuesSignUpChange(emailValue: String, passwordValue: String, usernameValue: String) {
    _email.value = emailValue
    _password.value = passwordValue
    _username.value = usernameValue
    checkIfFormIsValid()
  }

  private fun checkIfEmailIsValid() {
    _validEmail.value = Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
  }

  private fun checkIfUsernameIsValid(): Boolean {
    return (_username.value.length > 5)
  }

  private fun checkIfFormIsValid() {
    checkIfEmailIsValid()
    _formError.value =
      (_validEmail.value && _password.value.length > 6 && checkIfUsernameIsValid())
    _validUsername.value = checkIfUsernameIsValid()
  }

  fun goToPoliciesScreen() {
    _showDialog.value = false
  }

  @RequiresApi(VERSION_CODES.O) fun signUpIntent() {
    _showDialog.value = false
    viewModelScope.launch {
      firebaseAuthInteractor.doSignUp(_email.value, _password.value).collect { result ->
        when (result) {
          is Success -> {
            createUserDatabase(provider = "email")
            _signUpState.send(SignUpState(isSuccess = "Sign Up Success"))
          }

          is Loading -> {
            _signUpState.send(SignUpState(isLoading = true))
          }

          is Error -> {
            _signUpState.send(SignUpState(isError = result.message))
          }
        }
      }
    }
  }

  @RequiresApi(VERSION_CODES.O)
  private fun createUserDatabase(provider: String) {
    viewModelScope.launch {
      firebaseFirestoreInteractor.createUserDatabase(_email.value, _username.value, provider).collect { result ->
        when (result) {
          is Success -> {
            _signUpState.send(SignUpState(isSuccess = "Sign Up and DB created successfully"))
          }
          is Error -> {
            deleteCurrentUser()
            _signUpState.send(SignUpState(isError = "Error, please try again later"))
          }
          is Loading -> {/* not necessary */}
        }
      }
    }
  }

  private fun deleteCurrentUser() {
    firebaseAuthInteractor.deleteCurrentUser()
  }

  fun showPrivacyDialog() {
    _showDialog.value = true
  }

  fun clearResource() {
    viewModelScope.launch {
      _signUpState.send(SignUpState())
    }
  }
}