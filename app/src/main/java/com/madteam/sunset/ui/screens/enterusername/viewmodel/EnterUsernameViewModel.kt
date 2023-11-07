package com.madteam.sunset.ui.screens.enterusername.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.ui.screens.enterusername.state.EnterUsernameUIEvent
import com.madteam.sunset.ui.screens.enterusername.state.EnterUsernameUIState
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterUsernameViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state: MutableStateFlow<EnterUsernameUIState> =
        MutableStateFlow(EnterUsernameUIState())
    val state: StateFlow<EnterUsernameUIState> = _state

    fun onEvent(event: EnterUsernameUIEvent) {
        when (event) {
            is EnterUsernameUIEvent.UsernameChanged -> {
                _state.value = _state.value.copy(username = event.username)
                validateUsername()
            }

            is EnterUsernameUIEvent.ContinueClicked -> {
                _state.value = _state.value.copy(formEnabled = false, showDialog = true)
            }

            is EnterUsernameUIEvent.SetShowDialog -> {
                _state.value = _state.value.copy(showDialog = event.showDialog)
            }

            is EnterUsernameUIEvent.AcceptDialogClicked -> createDatabaseIntent()
            is EnterUsernameUIEvent.ClearState -> {
                _state.value = _state.value.copy(
                    signUpState = Resource.Success("")
                )
            }
        }
    }

    private fun validateUsername() {
        if (_state.value.username.length > 5) {
            _state.value = _state.value.copy(usernameIsValid = true)
        }
    }

    private fun createDatabaseIntent() {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            databaseRepository.createUser(
                email = currentUser!!.email!!,
                username = _state.value.username,
                provider = currentUser.providerId
            ).collectLatest {
                when (it) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(signUpState = Resource.Error(it.message!!))
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(signUpState = Resource.Loading())
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(signUpState = Resource.Success("Success"))
                    }
                }
            }
        }
    }

}