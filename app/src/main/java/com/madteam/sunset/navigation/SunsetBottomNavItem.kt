package com.madteam.sunset.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import com.madteam.sunset.R

sealed class SunsetBottomNavItem(var title: Int, var icon: ImageVector, var route: String) {

  data object Profile :
    SunsetBottomNavItem(
      R.string.menu_tab_profile,
      Icons.Default.Person,
      SunsetRoutes.MyProfileScreen.route
    )

  data object Discover :
    SunsetBottomNavItem(
      R.string.menu_tab_discover,
      Icons.Default.LocationOn,
      SunsetRoutes.DiscoverScreen.route
    )

  data object SunsetPrediction : SunsetBottomNavItem(
    R.string.menu_tab_prediction,
    Icons.Default.WbSunny,
    SunsetRoutes.SunsetPredictionScreen.route
  )

  data object Home :
    SunsetBottomNavItem(R.string.menu_tab_home, Icons.Filled.Home, SunsetRoutes.HomeScreen.route)

}
