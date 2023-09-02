package com.madteam.sunset.ui.screens.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.model.SpotPost
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.DatabaseContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    private val _userPosts: MutableStateFlow<List<SpotPost>> = MutableStateFlow(mutableListOf())
    val userPosts: StateFlow<List<SpotPost>> = _userPosts

    val navigateWelcomeScreen = MutableStateFlow(false)

    init {
        initUI()
    }

    private fun initUI() {
        authRepository.getCurrentUser()?.let { user ->
            databaseRepository.getUserByEmail(user.email!!) {
                _userInfo.value = it
                getUserPosts()
            }
        }
    }

    fun onTabClicked(tabClicked: Int) {
        _selectedTab.value = tabClicked
    }

    private fun getUserPosts() {
        viewModelScope.launch {
            databaseRepository.getSpotPostsByUsername(_userInfo.value.username).collectLatest {
                _userPosts.value = it
            }
        }
    }

    fun logOut() {
        authRepository.logout()
        navigateWelcomeScreen.value = true
    }
}