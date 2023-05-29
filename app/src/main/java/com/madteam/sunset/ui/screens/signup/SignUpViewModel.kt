package com.madteam.sunset.ui.screens.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.DatabaseContract
import com.madteam.sunset.utils.Result
import com.madteam.sunset.utils.fold
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

    val isValidForm = MutableStateFlow(false)

    private val _signUpState = MutableStateFlow<Result<AuthResult?>>(Result.Success(null))
    val signUpState: StateFlow<Result<AuthResult?>> = _signUpState

    private val _notifyUser = MutableStateFlow<Result<String>>(Result.Success(""))
    val notifyUser: StateFlow<Result<String>> = _notifyUser

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isUsernameValid(username: String): Boolean {
        return (username.length > 5)
    }

    private fun isPasswordValid(password: String): Boolean {
        return (password.length > MIN_PASSWORD_LENGTH)
    }

    fun isValidForm(email: String, password: String, username: String) {
        isValidForm.value =
            (isEmailValid(email) && isUsernameValid(username) && isPasswordValid(password))
    }

    fun goToPoliciesScreen() {
        //TODO: Go to policies Screen
    }

    fun signUpIntent(email: String, password: String, username: String) = viewModelScope.launch {

        _signUpState.value = authRepository.doSignUpWithPasswordAndEmail(email, password).also { result ->
            result.fold(
                onSuccess = { authResult ->
                    _notifyUser.value = Result.Loading()

                    val (userEmail, userProvider) = authResult?.user?.email to authResult?.user?.providerId
                    if (userEmail != null && userProvider != null) {
                        _notifyUser.value = databaseRepository.createUser(userEmail, username, userProvider)
                    } else {
                        _notifyUser.value = Result.Error(Throwable("Email or Provider invalid"))
                    }
                },
                onError = {},
                onLoading = {}
            )
        }
    }

//        authRepository.doSignUpWithPasswordAndEmail(email, password).fold(
//            onSuccess = { authResult ->
//                if (authResult != null) {
//                    createUserDatabase(authResult, username)
//                } else {
//
//                }
//            },
//            onError = { error ->
//                signUpState.value = Error(error)
//            },
//            onLoading = {
//
//            }
//        )
//        signUpState.value = Success(null)

    private fun deleteCurrentUser() = viewModelScope.launch {
        authRepository.deleteCurrentUser().collectLatest { }
    }

    fun clearSignUpState() {
        _signUpState.value = Result.Success(null)
    }
}