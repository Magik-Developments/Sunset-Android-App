package com.madteam.sunset.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.madteam.sunset.ui.common.SunsetBottomNavigation

@Composable
fun HomeScreen(
  navController: NavController
) {

  Scaffold(
    bottomBar = { SunsetBottomNavigation(navController) },
    content = { paddingValues ->
      Box(
        modifier = Modifier.padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        HomeContent()
      }
    }
  )
}

@Composable
fun HomeContent() {
}

@Composable
@Preview
fun MyProfileScreenPreview() {
  HomeContent()
}
