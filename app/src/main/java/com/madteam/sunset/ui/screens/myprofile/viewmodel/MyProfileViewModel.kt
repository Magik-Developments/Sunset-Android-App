package com.madteam.sunset.ui.screens.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.data.repositories.AuthContract
import com.madteam.sunset.data.repositories.DatabaseContract
import com.madteam.sunset.domain.usecases.userprofile.GetMyUserProfileInfoUseCase
import com.madteam.sunset.ui.screens.myprofile.state.MyProfileUIEvent
import com.madteam.sunset.ui.screens.myprofile.state.MyProfileUIState
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

    private val _state: MutableStateFlow<MyProfileUIState> = MutableStateFlow(MyProfileUIState())
    val state: StateFlow<MyProfileUIState> = _state

    init {
        initUI()
    }

    fun onEvent(event: MyProfileUIEvent) {
        when (event) {
            is MyProfileUIEvent.TabClicked -> {
                onTabClicked(event.tabClicked)
            }

            is MyProfileUIEvent.ShowExitDialog -> {
                setShowExitDialog(event.state)
            }

            is MyProfileUIEvent.LogOut -> {
                logOut()
            }

            is MyProfileUIEvent.UpdateUserInfo -> {
                updateUserInfo(event.userInfo)
            }
        }
    }

    private fun initUI() {
        viewModelScope.launch {
            getMyUserProfileInfoUseCase().let {
                _state.value = _state.value.copy(userInfo = it)
                getUserPosts()
                getUserSpots()
            }
        }
    }

    private fun onTabClicked(tabClicked: Int) {
        _state.value = _state.value.copy(selectedTab = tabClicked)
    }

    private fun setShowExitDialog(state: Boolean) {
        _state.value = _state.value.copy(showLogoutDialog = state)
    }

    private fun updateUserInfo(userProfile: UserProfile) {
        _state.value = _state.value.copy(
            userInfo = UserProfile(
                username = _state.value.userInfo.username,
                email = _state.value.userInfo.email,
                provider = _state.value.userInfo.provider,
                creation_date = _state.value.userInfo.creation_date,
                admin = _state.value.userInfo.admin,
                name = userProfile.name,
                location = userProfile.location,
                image = userProfile.image
            )
        )
    }

    private fun getUserPosts() {
        viewModelScope.launch {
            databaseRepository.getSpotPostsByUsername(_state.value.userInfo.username)
                .collectLatest {
                    _state.value = _state.value.copy(userPosts = it)
                }
        }
    }

    private fun getUserSpots() {
        viewModelScope.launch {
            databaseRepository.getSpotsByUsername(_state.value.userInfo.username).collectLatest {
                _state.value = _state.value.copy(userSpots = it)
            }
        }
    }

    private fun logOut() {
        authRepository.logout()
    }
}