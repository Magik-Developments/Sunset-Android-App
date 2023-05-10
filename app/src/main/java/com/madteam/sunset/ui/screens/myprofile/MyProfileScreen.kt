package com.madteam.sunset.ui.screens.myprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.FollowsUserStates
import com.madteam.sunset.ui.common.MenuIconButton
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.common.ThinButtonLight
import com.madteam.sunset.ui.common.UserLocationText
import com.madteam.sunset.ui.common.UserNameText
import com.madteam.sunset.ui.common.UserUsernameText
import com.madteam.sunset.ui.screens.editprofile.BottomSheetEditProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyProfileScreen(
  navController: NavController,
  viewModel: MyProfileViewModel = hiltViewModel(),
) {

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val editProfileModalState =
    ModalBottomSheetState(initialValue = Hidden, isSkipHalfExpanded = true)
  val username by viewModel.username.collectAsStateWithLifecycle()
  val navigateUp by viewModel.navigateUp.collectAsStateWithLifecycle()

   if (navigateUp)
    navController.navigateUp()

  ModalBottomSheetLayout(
    sheetState = editProfileModalState,
    sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
    sheetElevation = 10.dp,
    sheetContent = { BottomSheetEditProfileScreen() }
  ) {
    Scaffold(
      bottomBar = { SunsetBottomNavigation() },
      content = { paddingValues ->
        Box(
          modifier = Modifier.padding(paddingValues),
          contentAlignment = Alignment.Center
        ) {
          MyProfileContent(
            username = username,
            onEditProfileClick = { coroutineScope.launch { editProfileModalState.show() } },
            logout = { viewModel.logOut() }
          )
        }
      }
    )
  }
}

@Composable
fun MyProfileContent(
  username: String,
  logout: () -> Unit,
  onEditProfileClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .background(Color.White)
  ) {
    CustomSpacer(size = 16.dp)
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      UserUsernameText(username = username)
      MenuIconButton(onClick = { /*TODO*/ })
    }
    CustomSpacer(size = 8.dp)
    ProfileImage(image = R.drawable.logo_degrade, size = 80.dp)
    CustomSpacer(size = 16.dp)
    UserNameText(userName = "Adrià Fernández Arans")
    CustomSpacer(size = 8.dp)
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      UserLocationText(location = "🗺️ Terrassa, BCN")
      ThinButtonLight(onClick = onEditProfileClick, text = R.string.edit_profile)
    }
    CustomSpacer(size = 8.dp)
    FollowsUserStates()
    CustomSpacer(size = 48.dp)
    SmallButtonDark(
      onClick = logout,
      text = R.string.log_out,
      enabled = true
    )
  }
}

@Composable
@Preview
fun MyProfileScreenPreview() {
  MyProfileContent("My name", {}) {}
}
