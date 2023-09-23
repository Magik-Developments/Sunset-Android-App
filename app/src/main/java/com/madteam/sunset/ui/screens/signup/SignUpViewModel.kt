package com.madteam.sunset.ui.screens.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.DatabaseContract
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.Resource.Error
import com.madteam.sunset.utils.Resource.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_PASSWORD_LENGTH = 6

@HiltViewModel
class SignUpViewModel @Inject constructor(
  private val authRepository: AuthContract,
  private val databaseRepository: DatabaseContract,
) : ViewModel() {

  val isValidForm = MutableStateFlow(false)

  val signUpState = MutableStateFlow<Resource<AuthResult?>>(Success(null))

  fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
  }

  fun isUsernameValid(username: String): Boolean {
    return (username.length > 5)
  }

  private fun isPasswordValid(password: String): Boolean {
    return (password.length > MIN_PASSWORD_LENGTH)
  }

  fun isValidForm(email: String, password: String, username: String) {
    isValidForm.value =
      (isEmailValid(email) && isUsernameValid(username) && isPasswordValid(password))
  }

  fun signUpIntent(email: String, password: String, username: String) {
    viewModelScope.launch {
      authRepository.doSignUpWithPasswordAndEmail(email, password).collectLatest { result ->
        when (result) {
          is Success -> {
            createUserDatabase(result, username)
          }

          is Error -> {
            signUpState.value = Error(result.message.toString())
          }

          is Resource.Loading -> {
            signUpState.value = Resource.Loading()
          }

        }
      }
    }
    signUpState.value = Success(null)
  }

  private fun createUserDatabase(authResult: Resource<AuthResult?>, username: String) {
    viewModelScope.launch {
      val userEmail = authResult.data!!.user!!.email!!
      val userProvider = authResult.data.user!!.providerId
      databaseRepository.createUser(userEmail, username, userProvider).collectLatest { result ->
        when (result) {
          is Error -> {
            if (result.message == "e_user_already_exists") {
              signUpState.value = Error("e_user_already_exists")
            } else {
              signUpState.value = Error("Error signing up")
            }
            deleteCurrentUser()
          }

          is Success -> {
            signUpState.value = authResult
          }

          else -> { /* Not necessary */
          }
        }

      }
    }
  }

  private fun deleteCurrentUser() {
    viewModelScope.launch {
      authRepository.deleteCurrentUser().collectLatest { }
    }
  }

  fun clearSignUpState() {
    signUpState.value = Success(null)
  }
}