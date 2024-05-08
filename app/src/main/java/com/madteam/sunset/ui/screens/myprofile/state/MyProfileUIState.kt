package com.madteam.sunset.ui.screens.myprofile.state

import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.SpotPost
import com.madteam.sunset.data.model.UserProfile

data class MyProfileUIState(
    val selectedTab: Int = 0,
    val userInfo: UserProfile = UserProfile(),
    val userPosts: List<SpotPost> = mutableListOf(),
    val userSpots: List<Spot> = mutableListOf(),
    val showLogoutDialog: Boolean = false,
    val hasToLogOut: Boolean = false,
    val noLoggedDialog: Boolean = false,
)
