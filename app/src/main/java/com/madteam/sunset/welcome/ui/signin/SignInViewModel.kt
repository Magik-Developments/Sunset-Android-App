package com.madteam.sunset.welcome.ui.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.common.utils.Resource.Error
import com.madteam.sunset.common.utils.Resource.Loading
import com.madteam.sunset.common.utils.Resource.Success
import com.madteam.sunset.welcome.domain.interactor.FirebaseAuthInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val firebaseAuthInteractor: FirebaseAuthInteractor) :
  ViewModel() {

  private val _email = MutableStateFlow("")
  val email: StateFlow<String> = _email

  private val _password = MutableStateFlow("")
  val password: StateFlow<String> = _password

  private val _formError = MutableStateFlow(false)
  val formError: StateFlow<Boolean> = _formError

  private val _signInState = Channel<SignInState>()
  val signInState = _signInState.receiveAsFlow()

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

  fun signInWithEmailAndPasswordIntent() {
    viewModelScope.launch {
      firebaseAuthInteractor.doSignInWithPasswordAndEmail(_email.value, _password.value)
        .collect { result ->
          when (result) {
            is Success -> {
              _signInState.send(SignInState(isSuccess = "Sign In Success ${result.data?.user?.email}"))
            }
            is Error -> {
              _signInState.send(SignInState(isError = result.message.toString()))
            }
            is Loading -> {
              _signInState.send(SignInState(isLoading = true))
            }
          }
        }
    }
  }

  fun clearResource() {
    viewModelScope.launch {
      _signInState.send(SignInState())
    }
  }
}