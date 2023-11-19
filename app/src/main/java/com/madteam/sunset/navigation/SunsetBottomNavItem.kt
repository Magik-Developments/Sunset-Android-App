package com.madteam.sunset.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

sealed class SunsetBottomNavItem(var title: String, var icon: ImageVector, var route: String){

  data object Profile :
    SunsetBottomNavItem("Profile", Icons.Default.Person, SunsetRoutes.MyProfileScreen.route)

  data object Discover :
    SunsetBottomNavItem("Discover", Icons.Default.AddCircle, SunsetRoutes.DiscoverScreen.route)

  data object SunsetPrediction : SunsetBottomNavItem(
    "Sunset prediction",
    Icons.Default.WbSunny,
    SunsetRoutes.SunsetPredictionScreen.route
  )

  data object Home : SunsetBottomNavItem("Home", Icons.Filled.Home, SunsetRoutes.HomeScreen.route)

}
