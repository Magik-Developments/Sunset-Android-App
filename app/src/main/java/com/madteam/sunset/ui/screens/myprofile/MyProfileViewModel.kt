package com.madteam.sunset.ui.screens.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.DatabaseContract
import com.madteam.sunset.utils.Resource.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
  private val authRepository: AuthContract,
  private val databaseRepository: DatabaseContract
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
      databaseRepository.getProfileByUsername(_currentUserEmail.value)
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
      authRepository.getCurrentUser()
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