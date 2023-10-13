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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.R
import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.SpotPost
import com.madteam.sunset.data.model.SunsetTimeResponse
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.SunsetInfoModule
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
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
                    postLikeClick = viewModel::modifyUserPostLike,
                    loadNextSpotsPage = viewModel::loadNextSpotsPage,
                    loadNextPostsPage = viewModel::loadNextPostsPage
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
    postLikeClick: (String) -> Unit,
    loadNextSpotsPage: () -> Unit,
    loadNextPostsPage: () -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
            .verticalScroll(scrollState)
    ) {
        if (remainingTimeToSunset.isNotEmpty()) {
            CustomSpacer(size = 16.dp)
            Text(
                text = stringResource(id = R.string.dont_miss_sunset),
                style = primaryBoldHeadlineS,
                color = Color(0xFF333333),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            )
            CustomSpacer(size = 16.dp)
            SunsetInfoModule(
                sunsetTimeInformation = sunsetTimeInformation,
                userLocality = userLocality,
                remainingTimeToSunset = remainingTimeToSunset,
                clickToExplore = { navigateTo(SunsetRoutes.DiscoverScreen.route) },
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.last_spots_posted),
            style = primaryBoldHeadlineS,
            color = Color(0xFF333333),
            modifier = Modifier
                .padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 16.dp)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                CustomSpacer(size = 2.dp)
            }
            if (spotsList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .size(370.dp)
                            .background(
                                shimmerBrush(showShimmer = true),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clip(RoundedCornerShape(20.dp)),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {}
                }
            }
            itemsIndexed(spotsList) { index, item ->
                FeedSpotItem(
                    spotInfo = item,
                    userInfo = userInfo,
                    spotLikeClick = { spotLikeClick(item.id) },
                    onSpotClicked = { navigateTo("spot_detail_screen/spotReference=${item.id}") }
                )
                if (index == spotsList.size - 1) {
                    loadNextSpotsPage()
                }
            }
        }
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.last_posts),
            style = primaryBoldHeadlineS,
            color = Color(0xFF333333),
            modifier = Modifier
                .padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 16.dp)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                CustomSpacer(size = 2.dp)
            }
            if (postsList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .size(370.dp)
                            .background(
                                shimmerBrush(showShimmer = true),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clip(RoundedCornerShape(20.dp)),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {}
                }
            }
            itemsIndexed(postsList) { index, item ->
                FeedPostItem(
                    postInfo = item,
                    userInfo = userInfo,
                    postLikeClick = { postLikeClick(item.id) },
                    onPostClicked = { navigateTo("post_screen/postReference=${item.id}") }
                )
                if (index == postsList.size - 1) {
                    loadNextPostsPage()
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
