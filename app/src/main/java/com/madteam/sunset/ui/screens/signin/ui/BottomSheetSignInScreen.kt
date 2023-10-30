package com.madteam.sunset.ui.screens.signin.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R.string
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CardHandler
import com.madteam.sunset.ui.common.CardSubtitle
import com.madteam.sunset.ui.common.CardTitle
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.EmailTextField
import com.madteam.sunset.ui.common.ForgotPasswordText
import com.madteam.sunset.ui.common.OtherLoginIconButtons
import com.madteam.sunset.ui.common.OtherLoginMethodsSection
import com.madteam.sunset.ui.common.PasswordTextField
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.screens.signin.state.SignInUIEvent
import com.madteam.sunset.ui.screens.signin.state.SignInUIState
import com.madteam.sunset.ui.screens.signin.viewmodel.SignInViewModel
import com.madteam.sunset.utils.BackPressHandler
import com.madteam.sunset.utils.Resource

const val CARD_HEIGHT = 0.67

@Composable
fun BottomSheetSignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel(),
    modalOptions: () -> Unit,
    hideModal: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BackPressHandler {
        hideModal()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp * CARD_HEIGHT).dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFB600)),
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
    ) {
        BottomSheetSignInContent(
            state = state,
            modalOptions = modalOptions,
            navigateTo = navController::navigate,
            updateEmail = { viewModel.onEvent(SignInUIEvent.UpdateEmail(it)) },
            updatePassword = { viewModel.onEvent(SignInUIEvent.UpdatePassword(it)) },
            signInButton = { viewModel.onEvent(SignInUIEvent.SignInIntent) },
            clearSignInState = { viewModel.onEvent(SignInUIEvent.ClearSignInState) },
            navController = navController
        )
    }
}

@Composable
fun BottomSheetSignInContent(
    state: SignInUIState,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    modalOptions: () -> Unit,
    navigateTo: (String) -> Unit,
    signInButton: () -> Unit,
    clearSignInState: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 36.dp)
    ) {
        CustomSpacer(size = 8.dp)
        CardHandler()
        CustomSpacer(size = 16.dp)
        CardTitle(string.welcome_back)
        CardSubtitle(string.enter_details_below)
        CustomSpacer(size = 16.dp)
        EmailTextField(
            emailValue = state.email,
            onValueChange = { user ->
                updateEmail(user)
            },
            isError = !state.isValidEmail && state.email.isNotBlank() || state.invalidCredentials,
            errorMessage = if (state.invalidCredentials) {
                null
            } else {
                string.not_valid_email_error
            }
        )
        CustomSpacer(size = 16.dp)
        PasswordTextField(
            passwordValue = state.password, onValueChange = { password ->
                updatePassword(password)
            },
            isError = state.invalidCredentials,
            errorMessage = if (state.invalidCredentials) {
                string.invalid_email_or_password
            } else {
                null
            }
        )
        CustomSpacer(size = 24.dp)
        SmallButtonDark(
            onClick = { signInButton() },
            text = string.sign_in,
            enabled = state.isValidForm
        )
        CustomSpacer(size = 16.dp)
        ForgotPasswordText(onClick = { navigateTo(SunsetRoutes.LostPasswordScreen.route) })
        CustomSpacer(size = 40.dp)
        OtherLoginMethodsSection(string.not_registered_yet_signup_with)
        CustomSpacer(size = 24.dp)
        OtherLoginIconButtons(firstMethod = {
            Toast.makeText(
                context,
                "Do Google Login",
                Toast.LENGTH_SHORT
            ).show()
        },
            secondMethod = { modalOptions() })
    }

    when (state.signInState) {
        is Resource.Loading -> {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {}
            ) {
                CircularLoadingDialog()
            }
        }

        is Resource.Success -> {
            if (state.signInState.data != null && state.signInState.data.user!!.isEmailVerified) {
                LaunchedEffect(key1 = state.signInState.data) {
                    navController.navigate(SunsetRoutes.MyProfileScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                clearSignInState()
            } else if (state.signInState.data != null && !state.signInState.data.user!!.isEmailVerified) {
                LaunchedEffect(key1 = state.signInState.data) {
                    navigateTo(
                        "verify_account_screen/pass=${state.password}"
                    )
                }
                clearSignInState()
            }
        }

        is Resource.Error -> {
            Box(contentAlignment = Alignment.Center) {
                Toast.makeText(context, "${state.signInState.message}", Toast.LENGTH_SHORT)
                    .show()
            }
            clearSignInState()
        }
    }
}
