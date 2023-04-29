package com.madteam.sunset.ui.screens.myprofile

import androidx.lifecycle.ViewModel
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.DatabaseContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val authRepository: AuthContract,
    private val databaseRepository: DatabaseContract
) : ViewModel() {

    val username = MutableStateFlow("")

    val navigateUp = MutableStateFlow(false) // Fixme: hacer esto bien...

    init {
        authRepository.getCurrentUser()?.let { user ->
            databaseRepository.getProfileByUsername(user.email!!) {
                username.value = it.email
            }
        }
    }

    fun logOut() {
        authRepository.logout()
        navigateUp.value = true
    }
}