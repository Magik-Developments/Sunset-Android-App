package com.madteam.sunset.ui.screens.editprofile.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.domain.usecases.userprofile.GetMyUserProfileInfoUseCase
import com.madteam.sunset.ui.screens.editprofile.state.EditProfileUIEvent
import com.madteam.sunset.ui.screens.editprofile.state.EditProfileUIState
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val getMyUserProfileInfoUseCase: GetMyUserProfileInfoUseCase
) : ViewModel() {

    private val _originalUsername: MutableStateFlow<String> = MutableStateFlow("")
    private val _originalEmail: MutableStateFlow<String> = MutableStateFlow("")
    private val _originalName: MutableStateFlow<String> = MutableStateFlow("")
    private val _originalLocation: MutableStateFlow<String> = MutableStateFlow("")
    private val _originalUserImage: MutableStateFlow<String> = MutableStateFlow("")

    private val _state: MutableStateFlow<EditProfileUIState> =
        MutableStateFlow(EditProfileUIState())
    val state: StateFlow<EditProfileUIState> = _state

    init {
        setInitialValues()
    }

    fun onEvent(event: EditProfileUIEvent) {
        when (event) {
            EditProfileUIEvent.ClearUploadProgress -> {
                clearUpdateProgressState()
            }

            EditProfileUIEvent.SaveData -> {
                updateData()
            }

            is EditProfileUIEvent.UpdateLocation -> {
                updateLocation(event.location)
            }

            is EditProfileUIEvent.UpdateName -> {
                updateName(event.name)
            }

            is EditProfileUIEvent.UpdateProfileImage -> {
                updateSelectedProfileImage(event.uri)
            }
        }
    }

    private fun setInitialValues() {
        viewModelScope.launch {
            getMyUserProfileInfoUseCase().let {
                when (it) {
                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        with(it.data!!) {
                            _originalUsername.value = username
                            _originalEmail.value = email
                            _originalName.value = name
                            _originalLocation.value = location
                            _originalUserImage.value = image
                            _state.value = _state.value.copy(
                                email = email,
                                name = name,
                                location = location,
                                userImage = image,
                                userIsAdmin = admin,
                                username = username
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkIfDataHasChanged() {
        _state.value = _state.value.copy(
            dataHasChanged = !(_originalLocation.value == _state.value.location &&
                    _originalName.value == _state.value.name &&
                    _originalUserImage.value == _state.value.userImage
                    )
        )
    }

    private fun updateName(newName: String) {
        _state.value = _state.value.copy(name = newName)
        checkIfDataHasChanged()
    }

    private fun updateLocation(newLocation: String) {
        _state.value = _state.value.copy(location = newLocation)
        checkIfDataHasChanged()
    }

    private fun clearUpdateProgressState() {
        _state.value = _state.value.copy(uploadProgress = Resource.Success(""))
    }

    private fun updateData() {
        var newUserImage = ""
        if (_state.value.userImage != _originalUserImage.value) {
            newUserImage = _state.value.userImage
        }
        val newUser = UserProfile(
            username = _state.value.username,
            email = _state.value.email,
            provider = "",
            creation_date = "",
            name = _state.value.name,
            location = _state.value.location,
            image = newUserImage,
            admin = _state.value.userIsAdmin
        )
        viewModelScope.launch {
            databaseRepository.updateUser(newUser).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            dataHasChanged = false
                        )
                        _originalUserImage.value = _state.value.userImage
                        _originalName.value = _state.value.name
                        _originalLocation.value = _state.value.location
                    }

                    else -> {
                        //Not implemented yet
                    }
                }
                _state.value = _state.value.copy(uploadProgress = result)
            }

        }
    }

    private fun updateSelectedProfileImage(uri: Uri) {
        _state.value = _state.value.copy(userImage = uri.toString())
        checkIfDataHasChanged()
    }

}