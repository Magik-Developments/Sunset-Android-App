package com.madteam.sunset.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.madteam.sunset.model.Spot
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
  LazyColumn(
    modifier = Modifier.padding(top = 24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    items(getSpots()) { spot ->
      ItemSpot(spot = spot)
    }
  }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemSpot(spot: Spot) {
  Card(elevation = 4.dp) {
    GlideImage(
      model = spot.image,
      contentDescription = "",
      modifier = Modifier
        .width(346.dp)
        .height(378.dp),
      contentScale = ContentScale.Crop
    )
  }
}

fun getSpots(): List<Spot> {
  return listOf(
    Spot(
      "Sant Jeroni",
      "Montserrat, Manresa.",
      "https://live.staticflickr.com/65535/49373147057_7b1272acbf_b.jpg"
    ),
    Spot(
      "Far de formentor",
      "Formentor, Mallorca.",
      "https://cs-static-pro.s3.amazonaws.com/cobi%2Fmedia%2Fpollensa.com%2Fcache%2F2c%2F60%2F2c60d1d8041179db4b033246554bd091.jpg"
    ),
    Spot(
      "Miami Beach",
      "Miami, FL. USA",
      "https://www.sobeachtours.com/wp-content/uploads/2022/06/sunset-miami.jpg"
    )
  )
}

@Composable
@Preview
fun MyProfileScreenPreview() {
  HomeContent()
}
