package com.madteam.sunset.ui.screens.editprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _originalUsername: MutableStateFlow<String> = MutableStateFlow("")
    private val _originalEmail: MutableStateFlow<String> = MutableStateFlow("")
    private val _originalName: MutableStateFlow<String> = MutableStateFlow("")
    private val _originalLocation: MutableStateFlow<String> = MutableStateFlow("")
    private val _originalUserImage: MutableStateFlow<String> = MutableStateFlow("")

    private val _dataHasChanged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val dataHasChanged: StateFlow<Boolean> = _dataHasChanged

    init {
        setInitialValues()
    }

    private fun setInitialValues() {
        authRepository.getCurrentUser()?.let { user ->
            databaseRepository.getUserByEmail(user.email!!) {
                _originalName.value = it.name
                _originalLocation.value = it.location
                _originalEmail.value = it.email
                _originalUserImage.value = it.image
                username.value = it.username
                email.value = it.email
                name.value = it.name
                location.value = it.location
                userImage.value = it.image
                userIsAdmin.value = it.admin
            }
        }
    }

    private fun checkIfDataHasChanged() {
        _dataHasChanged.value =
            !(_originalLocation.value == location.value &&
                    _originalName.value == name.value &&
                    _originalUserImage.value == userImage.value)
    }

    fun updateName(newName: String) {
        name.value = newName
        checkIfDataHasChanged()
    }

    fun updateLocation(newLocation: String) {
        location.value = newLocation
        checkIfDataHasChanged()
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
            _dataHasChanged.value = false
        }
    }

    fun updateSelectedProfileImage(uri: Uri) {
        userImage.value = uri.toString()
        checkIfDataHasChanged()
    }

}