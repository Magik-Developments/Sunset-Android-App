package com.madteam.sunset.ui.screens.home.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.R
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.LocationPermissionDialog
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.SunsetButton
import com.madteam.sunset.ui.common.SunsetInfoModule
import com.madteam.sunset.ui.screens.home.state.HomeUIEvent
import com.madteam.sunset.ui.screens.home.state.HomeUIState
import com.madteam.sunset.ui.screens.home.viewmodel.HomeViewModel
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.utils.getCurrentLocation
import com.madteam.sunset.utils.hasLocationPermission
import com.madteam.sunset.utils.shimmerBrush
import com.madteam.sunset.utils.switchTab
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { SunsetBottomNavigation(navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                HomeContent(
                    state = state,
                    navigateTo = navController::navigate,
                    updateUserLocation = { viewModel.onEvent(HomeUIEvent.UpdateUserLocation(it)) },
                    spotLikeClick = { viewModel.onEvent(HomeUIEvent.ModifyUserSpotLike(it)) },
                    postLikeClick = { viewModel.onEvent(HomeUIEvent.ModifyUserPostLike(it)) },
                    loadNextSpotsPage = { viewModel.onEvent(HomeUIEvent.LoadNextSpotsPage) },
                    loadNextPostsPage = { viewModel.onEvent(HomeUIEvent.LoadNextPostsPage) },
                    navigateToSunsetPrediction = { navController.switchTab(SunsetRoutes.SunsetPredictionScreen.route) },
                    setShowLocationPermissionDialog = {
                        viewModel.onEvent(
                            HomeUIEvent.ShowLocationPermissionDialog(
                                it
                            )
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun HomeContent(
    state: HomeUIState,
    navigateTo: (String) -> Unit,
    updateUserLocation: (LatLng) -> Unit,
    spotLikeClick: (String) -> Unit,
    postLikeClick: (String) -> Unit,
    loadNextSpotsPage: () -> Unit,
    loadNextPostsPage: () -> Unit,
    navigateToSunsetPrediction: () -> Unit,
    setShowLocationPermissionDialog: (Boolean) -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var permissionNotGranted by remember {
        mutableStateOf(false)
    }

    val requestLocationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                    }
                } else {
                    permissionNotGranted = true
                }
            })

    LaunchedEffect(state.sunsetTimeInformation) {
        delay(1000)
        if (hasLocationPermission(context)) {
            if (state.userLocation == LatLng(0.0, 0.0)) {
                getCurrentLocation(context) { lat, long ->
                    updateUserLocation(LatLng(lat, long))
                }
            }
        } else {
            setShowLocationPermissionDialog(true)
        }
    }

    if (state.showLocationPermissionDialog) {
        LocationPermissionDialog {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            setShowLocationPermissionDialog(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AnimatedVisibility(visible = state.remainingTimeToSunset.isNotEmpty()) {
            Column(
                Modifier.fillMaxSize()
            ) {
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
                    sunsetTimeInformation = state.sunsetTimeInformation,
                    userLocality = state.userLocality,
                    remainingTimeToSunset = state.remainingTimeToSunset,
                    clickToExplore = { navigateTo(SunsetRoutes.DiscoverScreen.route) },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
        CustomSpacer(size = 16.dp)
        SunsetButton(
            modifier = Modifier.padding(horizontal = 24.dp),
            onClick = {
                navigateToSunsetPrediction()
            },
            text = R.string.know_spot_quality,
            maxLines = 2
        )
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
            if (state.spotsList.isEmpty()) {
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
            itemsIndexed(state.spotsList) { index, item ->
                FeedSpotItem(
                    spotInfo = item,
                    userInfo = state.userInfo,
                    spotLikeClick = { spotLikeClick(item.id) },
                    onSpotClicked = { navigateTo("spot_detail_screen/spotReference=${item.id}") }
                )
                if (index == state.spotsList.size - 1) {
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
            if (state.postsList.isEmpty()) {
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
            itemsIndexed(state.postsList) { index, item ->
                FeedPostItem(
                    postInfo = item,
                    userInfo = state.userInfo,
                    postLikeClick = { postLikeClick(item.id) },
                    onPostClicked = { navigateTo("post_screen/postReference=${item.id}") }
                )
                if (index == state.postsList.size - 1) {
                    loadNextPostsPage()
                }
            }
        }
        CustomSpacer(size = 24.dp)
    }
}
