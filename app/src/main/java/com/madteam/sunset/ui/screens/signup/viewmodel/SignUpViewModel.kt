package com.madteam.sunset.ui.screens.signup.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.madteam.sunset.data.repositories.AuthContract
import com.madteam.sunset.data.repositories.DatabaseContract
import com.madteam.sunset.ui.screens.signup.state.SignUpUIEvent
import com.madteam.sunset.ui.screens.signup.state.SignUpUIState
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_PASSWORD_LENGTH = 6

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthContract,
    private val databaseRepository: DatabaseContract,
) : ViewModel() {

    private val _state: MutableStateFlow<SignUpUIState> = MutableStateFlow(SignUpUIState())
    val state: StateFlow<SignUpUIState> = _state

    fun onEvent(event: SignUpUIEvent) {
        when (event) {
            is SignUpUIEvent.UpdateEmail -> {
                _state.value = _state.value.copy(email = event.email)
                _state.value = _state.value.copy(emailAlreadyInUse = false)
                isValidForm(_state.value.email, _state.value.password, _state.value.username)
            }

            is SignUpUIEvent.UpdatePassword -> {
                _state.value = _state.value.copy(password = event.password)
                isValidForm(_state.value.email, _state.value.password, _state.value.username)
            }

            is SignUpUIEvent.UpdateUsername -> {
                _state.value = _state.value.copy(username = event.username)
                _state.value = _state.value.copy(usernameAlreadyInUse = false)
                isValidForm(_state.value.email, _state.value.password, _state.value.username)
            }

            is SignUpUIEvent.ClearSignUpState -> {
                clearSignUpState()
            }

            is SignUpUIEvent.SetShowDialog -> {
                _state.value = _state.value.copy(showDialog = event.showDialog)
            }

            SignUpUIEvent.SignUpIntent -> {
                signUpIntent(_state.value.email, _state.value.password, _state.value.username)
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isUsernameValid(username: String): Boolean {
        return (username.length > 5)
    }

    private fun isPasswordValid(password: String): Boolean {
        return (password.length > MIN_PASSWORD_LENGTH)
    }

    private fun isValidForm(email: String, password: String, username: String) {
        _state.value = _state.value.copy(
            isEmailValid = isEmailValid(email),
            isUsernameValid = isUsernameValid(username),
            isPasswordValid = isPasswordValid(password)
        )
        _state.value = _state.value.copy(
            isValidForm = (isEmailValid(email) && isUsernameValid(username) && isPasswordValid(
                password
            ))
        )
    }

    private fun signUpIntent(email: String, password: String, username: String) {
        viewModelScope.launch {
            authRepository.doSignUpWithPasswordAndEmail(email, password).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        createUserDatabase(result, username)
                    }

                    is Resource.Error -> {
                        if (result.message == "e_user_already_exists") {
                            _state.value = _state.value.copy(usernameAlreadyInUse = true)
                        } else if (result.message == "The email address is already in use by another account.") {
                            _state.value = _state.value.copy(emailAlreadyInUse = true)
                        }
                        clearSignUpState()
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(signUpState = Resource.Loading())
                    }

                }
            }
        }
        clearSignUpState()
    }

    private fun createUserDatabase(authResult: Resource<AuthResult?>, username: String) {
        viewModelScope.launch {
            val userEmail = authResult.data!!.user!!.email!!
            val userProvider = authResult.data.user!!.providerId
            databaseRepository.createUser(userEmail, username, userProvider)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            if (result.message == "e_user_already_exists") {
                                _state.value =
                                    _state.value.copy(signUpState = Resource.Error("e_user_already_exists"))
                            } else {
                                _state.value =
                                    _state.value.copy(signUpState = Resource.Error("Error signing up"))
                            }
                            deleteCurrentUser()
                        }

                        is Resource.Success -> {
                            _state.value = _state.value.copy(signUpState = authResult)
                        }

                        else -> {
                            _state.value = _state.value.copy(signUpState = Resource.Loading())
                        }
                    }

                }
        }
    }

    private fun deleteCurrentUser() {
        viewModelScope.launch {
            authRepository.deleteCurrentUser().collectLatest { }
        }
    }

    private fun clearSignUpState() {
        _state.value = _state.value.copy(signUpState = Resource.Success(null))
    }
}