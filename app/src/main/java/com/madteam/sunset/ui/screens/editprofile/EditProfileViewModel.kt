package com.madteam.sunset.ui.screens.editprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.data.repositories.AuthContract
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.utils.Resource
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

    private val _uploadProgress: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Success(""))
    val updloadProgress: StateFlow<Resource<String>> = _uploadProgress

    init {
        setInitialValues()
    }

    private fun setInitialValues() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                val userInfo = databaseRepository.getUserByEmail(user.email!!)
                userInfo.let {
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
    }

    private fun checkIfDataHasChanged() {
        _dataHasChanged.value =
            !(_originalLocation.value == location.value &&
                    _originalName.value == name.value &&
                    _originalUserImage.value == userImage.value
                    )
    }

    fun updateName(newName: String) {
        name.value = newName
        checkIfDataHasChanged()
    }

    fun updateLocation(newLocation: String) {
        location.value = newLocation
        checkIfDataHasChanged()
    }

    fun clearUpdateProgressState() {
        _uploadProgress.value = Resource.Success("")
    }

    fun updateData() {
        var newUserImage = ""
        if (userImage.value != _originalUserImage.value) {
            newUserImage = userImage.value
        }
        val newUser = UserProfile(
            username = username.value,
            email = email.value,
            provider = "",
            creation_date = "",
            name = name.value,
            location = location.value,
            image = newUserImage,
            admin = userIsAdmin.value
        )
        viewModelScope.launch {
            databaseRepository.updateUser(newUser).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _dataHasChanged.value = false
                        _originalUserImage.value = userImage.value
                        _originalName.value = name.value
                        _originalLocation.value = location.value
                    }

                    else -> {}
                }
                _uploadProgress.value = result
            }

        }
    }

    fun updateSelectedProfileImage(uri: Uri) {
        userImage.value = uri.toString()
        checkIfDataHasChanged()
    }

}