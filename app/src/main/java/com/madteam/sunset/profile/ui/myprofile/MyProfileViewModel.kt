package com.madteam.sunset.profile.ui.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.common.utils.Resource.Success
import com.madteam.sunset.welcome.domain.interactor.FirebaseAuthInteractor
import com.madteam.sunset.welcome.domain.interactor.FirebaseFirestoreInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
  private val firebaseFirestoreInteractor: FirebaseFirestoreInteractor,
  private val firebaseAuthInteractor: FirebaseAuthInteractor
) :
  ViewModel() {

  private val _currentUserEmail = MutableStateFlow("")
  val currentUserEmail: StateFlow<String> = _currentUserEmail

  private val _username = MutableStateFlow("")
  val username: StateFlow<String> = _username

  init {
    getProfileUsername()
  }

  private fun getProfileUsername() {
    getCurrentFirebaseUser()
    viewModelScope.launch {
      firebaseFirestoreInteractor.getProfileUsername(_currentUserEmail.value)
        .collect { result ->
          when (result) {
            is Success -> {
              _username.value = result.data.toString()
            }

            else -> {
              /* For now it's not necessary */
            }
          }
        }
    }
  }

  private fun getCurrentFirebaseUser() {
    viewModelScope.launch {
      firebaseAuthInteractor.getCurrentUser()
        .collect { result ->
          when (result) {
            is Success -> {
              _currentUserEmail.value = result.data?.email.toString()
            }

            else -> {
              /* For now it's not necessary */
            }
          }

        }
    }
  }
}