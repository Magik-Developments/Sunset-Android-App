package com.madteam.sunset.ui.screens.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthContract
) : ViewModel() {

    val signInState = MutableStateFlow<Resource<AuthResult>?>(null)
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
        authRepository.doSignInWithPasswordAndEmail(email, password).collectLatest {
            signInState.value = it
        }
    }
}