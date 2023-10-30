package com.madteam.sunset.ui.screens.myprofile.state

import com.madteam.sunset.data.model.UserProfile

sealed class MyProfileUIEvent {
    data class TabClicked(val tabClicked: Int) : MyProfileUIEvent()
    data class ShowExitDialog(val state: Boolean) : MyProfileUIEvent()
    data class UpdateUserInfo(val userInfo: UserProfile) : MyProfileUIEvent()
    data object LogOut : MyProfileUIEvent()
}
