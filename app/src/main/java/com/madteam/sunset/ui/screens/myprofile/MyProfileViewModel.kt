package com.madteam.sunset.ui.screens.myprofile

import androidx.lifecycle.ViewModel
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.DatabaseContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val authRepository: AuthContract,
    private val databaseRepository: DatabaseContract
) : ViewModel() {

    private val _selectedTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab

    private val _userInfo: MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile())
    val userInfo: StateFlow<UserProfile> = _userInfo

    val navigateWelcomeScreen = MutableStateFlow(false)

    init {
        initUI()
    }

    private fun initUI() {
        authRepository.getCurrentUser()?.let { user ->
            databaseRepository.getUserByEmail(user.email!!) {
                _userInfo.value = it
            }
        }
    }

    fun onTabClicked(tabClicked: Int) {
        _selectedTab.value = tabClicked
    }

    fun logOut() {
        authRepository.logout()
        navigateWelcomeScreen.value = true
    }
}