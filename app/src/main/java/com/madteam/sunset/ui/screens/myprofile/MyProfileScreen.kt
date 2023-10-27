package com.madteam.sunset.ui.screens.myprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditLocationAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.data.model.Spot
import com.madteam.sunset.data.model.SpotPost
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.BottomSheetSettingsMenu
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.ImagePostCardProfile
import com.madteam.sunset.ui.common.ImageSpotCardProfile
import com.madteam.sunset.ui.common.MyProfileTopAppBar
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.ProfilePostTypeTab
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.ThinButtonLight
import com.madteam.sunset.ui.screens.editprofile.ui.BottomSheetEditProfileScreen
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
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
    val settingsModalState =
        ModalBottomSheetState(initialValue = Hidden, isSkipHalfExpanded = true)
    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val userPosts by viewModel.userPosts.collectAsStateWithLifecycle()
    val userSpots by viewModel.userSpots.collectAsStateWithLifecycle()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsStateWithLifecycle()

    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetSettingsMenu(
                onLogOutClick = { viewModel.setShowExitDialog(true) },
                isUserAdmin = userInfo.admin,
                onReportsClick = { navController.navigate(SunsetRoutes.SeeReportsScreen.route) },
                onNotificationsClick = { navController.navigate(SunsetRoutes.NotificationsScreen.route) }
            )
        },
        sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
        sheetElevation = 10.dp,
        sheetState = settingsModalState
    ) {
        ModalBottomSheetLayout(
            sheetState = editProfileModalState,
            sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
            sheetElevation = 10.dp,
            sheetContent = {
                BottomSheetEditProfileScreen(
                    onCloseButton = { coroutineScope.launch { editProfileModalState.hide() } },
                    onProfileUpdated = viewModel::updateUserInfo
                )
            }
        ) {
            Scaffold(
                bottomBar = { SunsetBottomNavigation(navController) },
                topBar = {
                    MyProfileTopAppBar(
                        username = userInfo.username,
                        openMenuClick = { coroutineScope.launch { settingsModalState.show() } }
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
                            navigateTo = navController::navigate,
                            showLogoutDialog = showLogoutDialog,
                            setShowLogoutDialog = viewModel::setShowExitDialog,
                            logOut = viewModel::logOut
                        )
                    }
                }
            )
        }
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
    navigateTo: (String) -> Unit,
    showLogoutDialog: Boolean,
    setShowLogoutDialog: (Boolean) -> Unit,
    logOut: () -> Unit
) {

    if (showLogoutDialog) {
        DismissAndPositiveDialog(
            setShowDialog = { setShowLogoutDialog(it) },
            dialogTitle = R.string.log_out,
            dialogDescription = R.string.log_out_dialog,
            positiveButtonText = R.string.log_out,
            dismissButtonText = R.string.cancel,
            dismissClickedAction = { setShowLogoutDialog(false) },
            positiveClickedAction = {
                setShowLogoutDialog(false)
                logOut()
                navigateTo(SunsetRoutes.WelcomeScreen.route)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White)
    ) {
        CustomSpacer(size = 16.dp)
        ProfileImage(imageUrl = userInfo.image, size = 80.dp)
        CustomSpacer(size = 16.dp)
        if (userInfo.name.isNotBlank()) {
            Text(
                text = userInfo.name,
                style = secondarySemiBoldHeadLineS,
                color = Color(0xFF333333)
            )
        } else {
            Row(
                modifier = Modifier.clickable {
                    onEditProfileClick()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.complete_information_name),
                    style = secondarySemiBoldHeadLineS,
                    color = Color(0xFFd9d9d9)
                )
                CustomSpacer(size = 4.dp)
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Add your name button",
                    tint = Color(0xFFd9d9d9)
                )
            }
        }
        CustomSpacer(size = 8.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (userInfo.location.isNotBlank()) {
                Text(
                    text = userInfo.location,
                    style = secondaryRegularHeadlineS,
                    color = Color(0xFF333333)
                )
            } else {
                Row(
                    modifier = Modifier.clickable {
                        onEditProfileClick()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.complete_information_location),
                        style = secondaryRegularHeadlineS,
                        color = Color(0xFFd9d9d9)
                    )
                    CustomSpacer(size = 4.dp)
                    Icon(
                        imageVector = Icons.Outlined.EditLocationAlt,
                        contentDescription = "Add your location button",
                        tint = Color(0xFFd9d9d9)
                    )
                }

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
                    Column(
                        Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp),
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Posts not found"
                        )
                        CustomSpacer(size = 16.dp)
                        Text(
                            text = stringResource(id = R.string.no_posts_yet),
                            style = secondaryRegularBodyL
                        )
                    }
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
                    Column(
                        Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp),
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Posts not found"
                        )
                        CustomSpacer(size = 16.dp)
                        Text(
                            text = stringResource(id = R.string.no_discovered_spots_yet),
                            style = secondaryRegularBodyL
                        )
                    }
                }
            }

            2 -> {
                // TODO: Save spots on lists feature still not developed
            }
        }
        CustomSpacer(size = 8.dp)
    }
}


