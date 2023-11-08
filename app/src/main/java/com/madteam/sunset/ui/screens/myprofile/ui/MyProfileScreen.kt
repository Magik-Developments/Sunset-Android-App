package com.madteam.sunset.ui.screens.myprofile.ui

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
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditLocationAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
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
import com.madteam.sunset.ui.screens.myprofile.state.MyProfileUIEvent
import com.madteam.sunset.ui.screens.myprofile.state.MyProfileUIState
import com.madteam.sunset.ui.screens.myprofile.viewmodel.MyProfileViewModel
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
        ModalBottomSheetState(
            initialValue = Hidden,
            isSkipHalfExpanded = true,
            density = LocalDensity.current
        )
    val settingsModalState =
        ModalBottomSheetState(
            initialValue = Hidden,
            isSkipHalfExpanded = true,
            density = LocalDensity.current
        )
    val state by viewModel.state.collectAsStateWithLifecycle()

    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetSettingsMenu(
                onLogOutClick = { viewModel.onEvent(MyProfileUIEvent.ShowExitDialog(true)) },
                isUserAdmin = state.userInfo.admin,
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
                    onProfileUpdated = { viewModel.onEvent(MyProfileUIEvent.UpdateUserInfo(it)) }
                )
            }
        ) {
            Scaffold(
                bottomBar = { SunsetBottomNavigation(navController) },
                topBar = {
                    MyProfileTopAppBar(
                        username = state.userInfo.username,
                        openMenuClick = { coroutineScope.launch { settingsModalState.show() } }
                    )
                },
                content = { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        MyProfileContent(
                            state = state,
                            onEditProfileClick = { coroutineScope.launch { editProfileModalState.show() } },
                            onTabClicked = { viewModel.onEvent(MyProfileUIEvent.TabClicked(it)) },
                            navigateTo = navController::navigate,
                            setShowLogoutDialog = {
                                viewModel.onEvent(
                                    MyProfileUIEvent.ShowExitDialog(
                                        it
                                    )
                                )
                            },
                            logOut = { viewModel.onEvent(MyProfileUIEvent.LogOut) }
                        )
                    }
                }
            )
        }
    }

}

@Composable
fun MyProfileContent(
    state: MyProfileUIState,
    onEditProfileClick: () -> Unit,
    onTabClicked: (Int) -> Unit,
    navigateTo: (String) -> Unit,
    setShowLogoutDialog: (Boolean) -> Unit,
    logOut: () -> Unit
) {

    if (state.showLogoutDialog) {
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

    if (state.hasToLogOut) {
        DismissAndPositiveDialog(
            setShowDialog = {
                //Cannot dismiss dialog
            },
            dialogTitle = R.string.generic_error,
            dialogDescription = R.string.log_out_need_error,
            dismissButtonText = R.string.log_out,
            dismissClickedAction = {
                logOut()
                navigateTo(SunsetRoutes.WelcomeScreen.route)
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White)
    ) {
        CustomSpacer(size = 16.dp)
        ProfileImage(imageUrl = state.userInfo.image, size = 80.dp)
        CustomSpacer(size = 16.dp)
        if (state.userInfo.name.isNotBlank()) {
            Text(
                text = state.userInfo.name,
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
            if (state.userInfo.location.isNotBlank()) {
                Text(
                    text = state.userInfo.location,
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
        CustomSpacer(size = 24.dp)
        ProfilePostTypeTab(
            tabOptions = listOf("Posts", "Spots"),
            selectedTab = state.selectedTab,
            tabOnClick = { onTabClicked(it) }
        )
        CustomSpacer(size = 8.dp)
        when (state.selectedTab) {
            0 -> {
                if (state.userPosts.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        content = {
                            itemsIndexed(state.userPosts) { _, post ->
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
                if (state.userSpots.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        content = {
                            itemsIndexed(state.userSpots) { _, spot ->
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


