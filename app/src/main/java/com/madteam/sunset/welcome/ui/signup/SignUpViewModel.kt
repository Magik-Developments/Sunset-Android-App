package com.madteam.sunset.welcome.ui.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.common.utils.Resource
import com.madteam.sunset.welcome.domain.interactor.FirebaseAuthInteractor
import com.nulabinc.zxcvbn.Zxcvbn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val firebaseAuthInteractor: FirebaseAuthInteractor) :
    ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _formError = MutableStateFlow(false)
    val formError: StateFlow<Boolean> = _formError

    private val _validEmail = MutableStateFlow(false)
    val validEmail: StateFlow<Boolean> = _validEmail

    private val _validUsername = MutableStateFlow(false)
    val validUsername: StateFlow<Boolean> = _validUsername

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _signUpState = Channel<SingUpState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun onValuesSignUpChange(emailValue: String, passwordValue: String, usernameValue: String) {
        _email.value = emailValue
        _password.value = passwordValue
        _username.value = usernameValue
        checkIfFormIsValid()
    }

    private fun checkIfEmailIsValid() {
        _validEmail.value = Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }

    private fun checkIfUsernameIsValid(): Boolean {
        return (_username.value.length > 5)
        // TODO: validar con firebase si el usuario ya existe o no
    }

    private fun checkIfFormIsValid() {
        checkIfEmailIsValid()
        _formError.value =
            (_validEmail.value && _password.value.length > 6 && checkIfUsernameIsValid())
        _validUsername.value = checkIfUsernameIsValid()
    }

    fun goToPoliciesScreen() {
        _showDialog.value = false
    }

    fun signUpIntent() {
        _showDialog.value = false
        viewModelScope.launch {
            firebaseAuthInteractor.doSignUp(_email.value, _password.value).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _signUpState.send(SingUpState(isSuccess = "Sign Up Success"))
                    }

                    is Resource.Loading -> {
                        _signUpState.send(SingUpState(isLoading = true))
                    }

                    is Resource.Error -> {
                        _signUpState.send(SingUpState(isError = result.message))
                    }
                }
            }
        }
    }

    fun showPrivacyDialog() {
        _showDialog.value = true
    }

    fun clearResource() {
        viewModelScope.launch {
            _signUpState.send(SingUpState())
        }
    }
}