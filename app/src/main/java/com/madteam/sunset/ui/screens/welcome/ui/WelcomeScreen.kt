package com.madteam.sunset.ui.screens.welcome.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.madteam.sunset.R
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.GoogleButton
import com.madteam.sunset.ui.common.MainTitle
import com.madteam.sunset.ui.common.SubTitle
import com.madteam.sunset.ui.common.SunsetButton
import com.madteam.sunset.ui.common.SunsetLogoImage
import com.madteam.sunset.ui.screens.welcome.state.WelcomeUIEvent
import com.madteam.sunset.ui.screens.welcome.viewmodel.WelcomeViewModel
import com.madteam.sunset.utils.BackPressHandler
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val modalState = ModalBottomSheetState(
        initialValue = Hidden,
        isSkipHalfExpanded = true,
        density = LocalDensity.current
    )
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    viewModel.onEvent(WelcomeUIEvent.HandleGoogleSignInResult(task.result as GoogleSignInAccount))
                }
            } else {
                viewModel.onEvent(WelcomeUIEvent.ShowLoading(false))
            }
        }

    BackPressHandler {}

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp),
        sheetElevation = 10.dp,
        sheetContent = {
            BottomSheetLoginScreen(
                navController = navController,
                hideModal = {
                    coroutineScope.launch {
                        modalState.hide()
                    }
                }
            )
        }
    ) {
        WelcomeContent(
            onEmailClick = {
                coroutineScope.launch {
                    if (!modalState.isVisible) {
                        modalState.show()
                    } else {
                        modalState.hide()
                    }
                }
            },
            onGoogleClick = {
                viewModel.onEvent(WelcomeUIEvent.ShowLoading(true))
                startForResult.launch(viewModel.googleSignInClient.signInIntent)
            }
        )
    }

    if (state.isLoading) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {}
            ) {
                CircularLoadingDialog()
            }
        }
    }

    when (state.signInState) {
        is Resource.Loading -> {
            //Not implemented
        }

        is Resource.Success -> {
            if (state.signInState.data != null) {
                LaunchedEffect(key1 = state.signInState.data) {
                    navController.navigate(SunsetRoutes.MyProfileScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                viewModel.onEvent(WelcomeUIEvent.ClearSignInState)
            }
        }

        is Resource.Error -> {
            if (state.signInState.message == "e_google_user_first_time") {
                LaunchedEffect(key1 = state.signInState.message) {
                    navController.navigate(SunsetRoutes.EnterUsernameScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            } else {
                Box(contentAlignment = Alignment.Center) {
                    Toast.makeText(context, "${state.signInState.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            viewModel.onEvent(WelcomeUIEvent.ClearSignInState)
        }
    }
}

@Composable
fun WelcomeContent(
    onEmailClick: () -> Unit,
    onGoogleClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SunsetLogoImage()
        CustomSpacer(56.dp)
        MainTitle()
        CustomSpacer(size = 8.dp)
        SubTitle(Modifier.align(Alignment.Start))
        CustomSpacer(size = 56.dp)
        SunsetButton(text = R.string.btn_continue_email, onClick = onEmailClick)
        CustomSpacer(size = 16.dp)
        GoogleButton(onClick = onGoogleClick)
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPrev() {
    WelcomeContent({}, {})
}