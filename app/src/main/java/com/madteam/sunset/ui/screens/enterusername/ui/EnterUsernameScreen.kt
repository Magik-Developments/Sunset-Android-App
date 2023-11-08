package com.madteam.sunset.ui.screens.enterusername.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.madteam.sunset.R
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.ErrorIcon
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.SuccessIcon
import com.madteam.sunset.ui.common.UsernameTextField
import com.madteam.sunset.ui.screens.enterusername.state.EnterUsernameUIEvent
import com.madteam.sunset.ui.screens.enterusername.viewmodel.EnterUsernameViewModel
import com.madteam.sunset.ui.screens.signup.ui.POLICIES_URL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.utils.Resource

@Composable
fun EnterUsernameScreen(
    navController: NavController,
    viewModel: EnterUsernameViewModel = hiltViewModel()
) {

    val animComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.sunwithglasses))
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    val verticalScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
            .verticalScroll(verticalScroll),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (state.showDialog) {
            DismissAndPositiveDialog(
                setShowDialog = {
                    viewModel.onEvent(EnterUsernameUIEvent.SetShowDialog(false))
                },
                dismissClickedAction = {
                    uriHandler.openUri(POLICIES_URL)
                    viewModel.onEvent(EnterUsernameUIEvent.SetShowDialog(false))
                },
                dismissButtonText = R.string.read_policies,
                positiveButtonText = R.string.sign_up,
                dialogTitle = R.string.privacy_dialog_title,
                dialogDescription = R.string.privacy_dialog_description,
                image = R.drawable.sunset_vectorial_art_01,
                positiveClickedAction = {
                    viewModel.onEvent(EnterUsernameUIEvent.AcceptDialogClicked)
                    viewModel.onEvent(EnterUsernameUIEvent.SetShowDialog(false))
                })
        }

        CustomSpacer(size = 48.dp)
        LottieAnimation(
            modifier = Modifier
                .height(250.dp)
                .width(250.dp),
            composition = animComposition,
            iterations = LottieConstants.IterateForever
        )
        CustomSpacer(size = 8.dp)
        Text(
            text = stringResource(id = R.string.choose_username),
            style = primaryBoldHeadlineL,
            color = Color(0xFF333333)
        )
        CustomSpacer(size = 24.dp)
        Text(
            text = stringResource(id = R.string.choose_username_subtitle),
            style = primaryBoldHeadlineS,
            color = Color(0xFF999999),
            textAlign = TextAlign.Center
        )
        CustomSpacer(size = 40.dp)
        UsernameTextField(
            usernameValue = state.username,
            enabled = state.formEnabled,
            onValueChange = { username ->
                viewModel.onEvent(EnterUsernameUIEvent.UsernameChanged(username))
            },
            endIcon = {
                if (state.usernameIsValid) SuccessIcon() else if (state.username.isNotBlank()) {
                    ErrorIcon()
                }
            },
            isError = state.usernameAlreadyExists,
            errorMessage = R.string.user_already_exists
        )
        CustomSpacer(size = 24.dp)
        SmallButtonDark(onClick = {
            viewModel.onEvent(EnterUsernameUIEvent.ContinueClicked)
        }, text = R.string.continue_text, enabled = state.usernameIsValid)
    }

    when (state.signUpState) {
        is Resource.Error -> {
            //Not implemented
        }

        is Resource.Loading -> {
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

        is Resource.Success -> {
            if (state.signUpState.data != null && state.signUpState.data == "Success") {
                LaunchedEffect(key1 = state.signUpState.data) {
                    navController.navigate(SunsetRoutes.HomeScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                viewModel.onEvent(EnterUsernameUIEvent.ClearState)
            }
        }

    }
}