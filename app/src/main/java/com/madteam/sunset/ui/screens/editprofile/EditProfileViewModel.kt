package com.madteam.sunset.ui.screens.editprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
  private val authRepository: AuthContract,
  private val databaseRepository: DatabaseRepository
) : ViewModel() {

  val username = MutableStateFlow("")
  val email = MutableStateFlow("")
  val name = MutableStateFlow("")
  val location = MutableStateFlow("")
  val userImage = MutableStateFlow("")
  val userIsAdmin = MutableStateFlow(false)

  init {
    setInitialValues()
  }

  private fun setInitialValues() {
    authRepository.getCurrentUser()?.let { user ->
      databaseRepository.getUserByEmail(user.email!!) {
        username.value = it.username
        email.value = it.email
        name.value = it.name
        location.value = it.location
        userImage.value = it.image
        userIsAdmin.value = it.admin
      }
    }
  }

  fun updateName(newName: String) {
    name.value = newName
  }

  fun updateLocation(newLocation: String) {
    location.value = newLocation
  }

  fun updateData() {
    val newUser = UserProfile(
      username = username.value,
      email = email.value,
      provider = "",
      creation_date = "",
      name = name.value,
      location = location.value,
      image = userImage.value,
      admin = userIsAdmin.value
    )
    viewModelScope.launch {
      databaseRepository.updateUser(newUser).collectLatest {}
    }
  }

  fun updateSelectedProfileImage(uri: Uri) {
    userImage.value = uri.toString()
  }

}