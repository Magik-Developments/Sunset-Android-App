package com.madteam.sunset.ui.screens.myprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.model.Spot
import com.madteam.sunset.model.SpotPost
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.ImagePostCardProfile
import com.madteam.sunset.ui.common.ImageSpotCardProfile
import com.madteam.sunset.ui.common.MyProfileTopAppBar
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.ProfilePostTypeTab
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.ThinButtonLight
import com.madteam.sunset.ui.common.UserLocationText
import com.madteam.sunset.ui.common.UserNameText
import com.madteam.sunset.ui.screens.editprofile.BottomSheetEditProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyProfileScreen(
    navController: NavController,
    viewModel: MyProfileViewModel = hiltViewModel(),
) {

    val coroutineScope = rememberCoroutineScope()
    val editProfileModalState =
        ModalBottomSheetState(initialValue = Hidden, isSkipHalfExpanded = true)
    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
    val navigateUp by viewModel.navigateWelcomeScreen.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val userPosts by viewModel.userPosts.collectAsStateWithLifecycle()
    val userSpots by viewModel.userSpots.collectAsStateWithLifecycle()

    if (navigateUp)
        navController.navigate(SunsetRoutes.WelcomeScreen.route)

    ModalBottomSheetLayout(
        sheetState = editProfileModalState,
        sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
        sheetElevation = 10.dp,
        sheetContent = { BottomSheetEditProfileScreen() }
    ) {
        Scaffold(
            bottomBar = { SunsetBottomNavigation(navController) },
            topBar = {
                MyProfileTopAppBar(
                    username = userInfo.username,
                    isAdmin = userInfo.admin,
                    reportsNumbers = 0,
                    goToReportsScreen = { navController.navigate(SunsetRoutes.SeeReportsScreen.route) },
                    logOutClick = viewModel::logOut
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier.padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    MyProfileContent(
                        userInfo = userInfo,
                        onEditProfileClick = { coroutineScope.launch { editProfileModalState.show() } },
                        selectedTab = selectedTab,
                        onTabClicked = viewModel::onTabClicked,
                        userPosts = userPosts,
                        userSpots = userSpots,
                        navigateTo = navController::navigate
                    )
                }
            }
        )
    }
}

@Composable
fun MyProfileContent(
    userInfo: UserProfile,
    onEditProfileClick: () -> Unit,
    selectedTab: Int,
    onTabClicked: (Int) -> Unit,
    userPosts: List<SpotPost>,
    userSpots: List<Spot>,
    navigateTo: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White)
    ) {
        CustomSpacer(size = 16.dp)
        ProfileImage(imageUrl = userInfo.image, size = 80.dp)
        if (userInfo.name.isNotBlank()) {
            CustomSpacer(size = 16.dp)
            UserNameText(userName = userInfo.name)
            CustomSpacer(size = 8.dp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (userInfo.location.isNotBlank()) {
                Arrangement.SpaceBetween
            } else {
                Arrangement.Center
            }
        ) {
            if (userInfo.location.isNotBlank()) {
                UserLocationText(location = userInfo.location)
            }
            ThinButtonLight(onClick = onEditProfileClick, text = R.string.edit_profile)
        }
        CustomSpacer(size = 8.dp)
        // FollowsUserStates() TODO: User states won't be displayed until it has been developed
        CustomSpacer(size = 24.dp)
        ProfilePostTypeTab(
            tabOptions = listOf("Posts", "Spots"),
            selectedTab = selectedTab,
            tabOnClick = { onTabClicked(it) }
        )
        CustomSpacer(size = 8.dp)
        when (selectedTab) {
            0 -> {
                if (userPosts.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        content = {
                            itemsIndexed(userPosts) { _, post ->
                                ImagePostCardProfile(
                                    postInfo = post,
                                    onItemClicked = {
                                        navigateTo("post_screen/postReference=${post.id}")
                                    }
                                )
                            }
                        },
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    )
                } else {
                    // TODO: Show image or copy saying user hasn't posted anything yet
                }
            }

            1 -> {
                if (userSpots.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        content = {
                            itemsIndexed(userSpots) { _, spot ->
                                ImageSpotCardProfile(
                                    spotInfo = spot,
                                    onItemClicked = {
                                        navigateTo("spot_detail_screen/spotReference=${spot.id}")
                                    }
                                )
                            }
                        },
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    )
                } else {
                    // TODO: Show image or copy saying user has not discovered spots yet
                }
            }

            2 -> {
                // TODO: Save spots on lists feature still not developed
            }
        }
        CustomSpacer(size = 8.dp)
    }
}


