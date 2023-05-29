package com.madteam.sunset.ui.screens.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthContract
) : ViewModel() {

    private var _signInState = MutableStateFlow<Result<AuthResult?>>(Result.Success(null))
    var signInState: StateFlow<Result<AuthResult?>> = _signInState

    val isValidForm = MutableStateFlow(false)

    private fun isUserValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(pass: String): Boolean {
        return pass.isNotBlank() && pass.length >= 6
    }

    fun isValidForm(email: String, pass: String) {
        isValidForm.value = (isUserValid(email) && isPasswordValid(pass))
    }

    fun signInWithEmailAndPasswordIntent(email: String, password: String) = viewModelScope.launch {
        _signInState.value = Result.Loading()
        _signInState.value = authRepository.doSignInWithPasswordAndEmail(email, password)
    }

    fun clearSignInState() {
        signInState = MutableStateFlow(Result.Success(null))
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}