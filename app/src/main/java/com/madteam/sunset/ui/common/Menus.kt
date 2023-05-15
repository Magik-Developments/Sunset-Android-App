package com.madteam.sunset.ui.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.madteam.sunset.navigation.SunsetBottomNavItem
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.utils.shadow

@Composable
fun SunsetBottomNavigation(navController: NavController) {

  val unselectedContentColor = Color(0xB3FFB600)
  val selectedContentColor = Color(0xFF000000)

  val items = listOf(
    SunsetBottomNavItem.Home,
    SunsetBottomNavItem.Discover,
    SunsetBottomNavItem.Profile
  )

  var selected by remember { mutableStateOf(2) }
  BottomNavigation(
    modifier = Modifier
      .height(84.dp)
      .shadow(
        color = Color(0x33000000),
        blurRadius = 2.dp,
        offsetY = (-2).dp
      )
      .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
    backgroundColor = Color.White
  ) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    items.forEach { item ->
      BottomNavigationItem(
        selected = currentRoute == item.route,
        onClick = {
          navController.navigate(item.route) {
            navController.graph.startDestinationRoute?.let { screen_route ->
              popUpTo(screen_route) {
                saveState = true
              }
            }
            launchSingleTop = true
            restoreState = true
          }
        },
        icon = {
          Icon(
            imageVector = item.icon, contentDescription = item.title,
            modifier = Modifier
              .height(32.dp)
              .width(32.dp)
          )
        },
        unselectedContentColor = unselectedContentColor,
        selectedContentColor = selectedContentColor
      )
    }
  }
}

