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
    val name = MutableStateFlow("")
    val location = MutableStateFlow("")

    val navigateUp = MutableStateFlow(false) // Fixme: hacer esto bien...

    init {
        initUI()
    }

    fun initUI() {
        authRepository.getCurrentUser()?.let { user ->
            databaseRepository.getUserByEmail(user.email!!) {
                username.value = it.username
                name.value = it.name
                location.value = it.location
            }
        }
    }

    fun logOut() {
        authRepository.logout()
        navigateUp.value = true
    }
}