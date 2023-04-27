package com.madteam.sunset.welcome.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.welcome.ui.signin.SignInState
import com.madteam.sunset.welcome.ui.signup.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

  private val _signUpState = Channel<SignUpState>()
  val signUpState = _signUpState.receiveAsFlow()

  private val _signInState = Channel<SignInState>()
  val signInState = _signInState.receiveAsFlow()

  fun updateSignUpState(state: SignUpState) {
    _signUpState.trySend(state)
  }

  fun updateSignInState(state: SignInState) {
    viewModelScope.launch {
      _signInState.send(state)
    }
  }

  fun clearResource() {
    viewModelScope.launch {
      _signInState.send(SignInState())
    }
  }



}