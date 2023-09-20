package com.madteam.sunset.ui.screens.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.model.Spot
import com.madteam.sunset.model.SpotPost
import com.madteam.sunset.model.SunsetTimeResponse
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.SunsetInfoModule
import com.madteam.sunset.utils.getCurrentLocation
import com.madteam.sunset.utils.shimmerBrush

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {

    val sunsetTimeInformation by viewModel.sunsetTimeInformation.collectAsStateWithLifecycle()
    val userLocality by viewModel.userLocality.collectAsStateWithLifecycle()
    val remainingTimeToSunset by viewModel.remainingTimeToSunset.collectAsStateWithLifecycle()
    val spotsList by viewModel.spotsList.collectAsStateWithLifecycle()
    val postsList by viewModel.postsList.collectAsStateWithLifecycle()
    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { SunsetBottomNavigation(navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                HomeContent(
                    sunsetTimeInformation = sunsetTimeInformation,
                    navigateTo = navController::navigate,
                    remainingTimeToSunset = remainingTimeToSunset,
                    updateUserLocation = viewModel::updateUserLocation,
                    userLocality = userLocality,
                    spotsList = spotsList,
                    userInfo = userInfo,
                    spotLikeClick = viewModel::modifyUserSpotLike,
                    postsList = postsList,
                    postLikeClick = viewModel::modifyUserPostLike
                )
            }
        }
    )
}

@Composable
fun HomeContent(
    sunsetTimeInformation: SunsetTimeResponse,
    navigateTo: (String) -> Unit,
    remainingTimeToSunset: String,
    updateUserLocation: (LatLng) -> Unit,
    userLocality: String,
    spotsList: List<Spot>,
    userInfo: UserProfile,
    spotLikeClick: (String) -> Unit,
    postsList: List<SpotPost>,
    postLikeClick: (String) -> Unit
) {

    val context = LocalContext.current

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                    }
                }
            }
        )

    LaunchedEffect(key1 = sunsetTimeInformation) {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                if (remainingTimeToSunset.isNotEmpty()) {
                    CustomSpacer(size = 24.dp)
                    SunsetInfoModule(
                        sunsetTimeInformation = sunsetTimeInformation,
                        userLocality = userLocality,
                        remainingTimeToSunset = remainingTimeToSunset,
                        clickToExplore = { navigateTo(SunsetRoutes.DiscoverScreen.route) }
                    )
                }
            }
            itemsIndexed(spotsList) { _, spot ->
                FeedSpotItem(
                    spotInfo = spot,
                    userInfo = userInfo,
                    spotLikeClick = { spotLikeClick(spot.id) },
                    onSpotClicked = { navigateTo("spot_detail_screen/spotReference=${spot.id}") }
                )
            }
            itemsIndexed(postsList) { _, post ->
                FeedPostItem(
                    postInfo = post,
                    userInfo = userInfo,
                    postLikeClick = { postLikeClick(post.id) },
                    onPostClicked = { navigateTo("post_screen/postReference=${post.id}") }
                )
            }
            if (spotsList.isEmpty() && postsList.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(370.dp)
                            .background(shimmerBrush(), shape = RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        Column(Modifier.fillMaxSize()) {

                        }
                    }
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(370.dp)
                            .background(shimmerBrush(), shape = RoundedCornerShape(20.dp))
                    ) {}
                }
            }
        }
        CustomSpacer(size = 24.dp)
    }
}

@Composable
@Preview
fun MyProfileScreenPreview() {

}
