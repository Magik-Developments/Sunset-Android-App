package com.madteam.sunset.ui.screens.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.SpotPost
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.data.repositories.AuthContract
import com.madteam.sunset.data.repositories.DatabaseContract
import com.madteam.sunset.domain.usecases.GetMyUserProfileInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val authRepository: AuthContract,
    private val databaseRepository: DatabaseContract,
    private val getMyUserProfileInfoUseCase: GetMyUserProfileInfoUseCase
) : ViewModel() {

    private val _selectedTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab

    private val _userInfo: MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile())
    val userInfo: StateFlow<UserProfile> = _userInfo

    private val _userPosts: MutableStateFlow<List<SpotPost>> = MutableStateFlow(mutableListOf())
    val userPosts: StateFlow<List<SpotPost>> = _userPosts

    private val _userSpots: MutableStateFlow<List<Spot>> = MutableStateFlow(mutableListOf())
    val userSpots: StateFlow<List<Spot>> = _userSpots

    private val _showLogoutDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog

    init {
        initUI()
    }

    private fun initUI() {
        viewModelScope.launch {
            getMyUserProfileInfoUseCase().let {
                _userInfo.value = it
                getUserPosts()
                getUserSpots()
            }
        }
    }

    fun onTabClicked(tabClicked: Int) {
        _selectedTab.value = tabClicked
    }

    fun setShowExitDialog(state: Boolean) {
        _showLogoutDialog.value = state
    }

    fun updateUserInfo(userProfile: UserProfile) {
        _userInfo.value = UserProfile(
            username = _userInfo.value.username,
            email = _userInfo.value.email,
            provider = _userInfo.value.provider,
            creation_date = _userInfo.value.creation_date,
            admin = _userInfo.value.admin,
            name = userProfile.name,
            location = userProfile.location,
            image = userProfile.image
        )
    }

    private fun getUserPosts() {
        viewModelScope.launch {
            databaseRepository.getSpotPostsByUsername(_userInfo.value.username).collectLatest {
                _userPosts.value = it
            }
        }
    }

    private fun getUserSpots() {
        viewModelScope.launch {
            databaseRepository.getSpotsByUsername(_userInfo.value.username).collectLatest {
                _userSpots.value = it
            }
        }
    }

    fun logOut() {
        authRepository.logout()
    }
}