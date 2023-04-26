package com.madteam.sunset.profile.ui.myprofile

import androidx.lifecycle.ViewModel
import com.madteam.sunset.welcome.domain.interactor.FirebaseFirestoreInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
  private val firebaseFirestoreInteractor: FirebaseFirestoreInteractor
) :
  ViewModel() {

  private val _username = MutableStateFlow("")
  val username: StateFlow<String> = _username

  private fun getProfileUsername(): String {
    firebaseFirestoreInteractor.getProfileUsername()
  }
}