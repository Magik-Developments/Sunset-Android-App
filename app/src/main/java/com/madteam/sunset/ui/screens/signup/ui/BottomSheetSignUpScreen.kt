package com.madteam.sunset.ui.screens.signup.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.R.string
import com.madteam.sunset.ui.common.CardHandler
import com.madteam.sunset.ui.common.CardSubtitle
import com.madteam.sunset.ui.common.CardTitle
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.EmailTextField
import com.madteam.sunset.ui.common.ErrorIcon
import com.madteam.sunset.ui.common.OtherLoginIconButtons
import com.madteam.sunset.ui.common.OtherLoginMethodsSection
import com.madteam.sunset.ui.common.PasswordTextField
import com.madteam.sunset.ui.common.PasswordVisibilityOffIcon
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.SuccessIcon
import com.madteam.sunset.ui.common.UsernameTextField
import com.madteam.sunset.ui.screens.signin.ui.CARD_HEIGHT
import com.madteam.sunset.ui.screens.signup.state.SignUpUIEvent
import com.madteam.sunset.ui.screens.signup.state.SignUpUIState
import com.madteam.sunset.ui.screens.signup.viewmodel.SignUpViewModel
import com.madteam.sunset.utils.BackPressHandler
import com.madteam.sunset.utils.Resource

const val POLICIES_URL = "https://sunsetapp.es/index.php/privacy-policy/"

@Composable
fun BottomSheetSignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
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
        backgroundColor = Color(0xFFFFB600),
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
    ) {
        BottomSheetSignUpContent(
            state = state,
            modalOptions = modalOptions,
            setShowDialog = { viewModel.onEvent(SignUpUIEvent.SetShowDialog(it)) },
            updateEmail = { viewModel.onEvent(SignUpUIEvent.UpdateEmail(it)) },
            updatePassword = { viewModel.onEvent(SignUpUIEvent.UpdatePassword(it)) },
            updateUsername = { viewModel.onEvent(SignUpUIEvent.UpdateUsername(it)) },
            acceptDialogClicked = { viewModel.onEvent(SignUpUIEvent.SignUpIntent) },
            clearSignUpState = { viewModel.onEvent(SignUpUIEvent.ClearSignUpState) },
            navigateTo = navController::navigate
        )
    }
}

@Composable
fun BottomSheetSignUpContent(
    state: SignUpUIState,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    updateUsername: (String) -> Unit,
    modalOptions: () -> Unit,
    acceptDialogClicked: () -> Unit,
    navigateTo: (String) -> Unit,
    clearSignUpState: () -> Unit,
    setShowDialog: (Boolean) -> Unit,
) {

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    if (state.showDialog) {
        DismissAndPositiveDialog(
            setShowDialog = { setShowDialog(it) },
            dismissClickedAction = {
                uriHandler.openUri(POLICIES_URL)
                setShowDialog(false)
            },
            dismissButtonText = string.read_policies,
            positiveButtonText = string.sign_up,
            dialogTitle = string.privacy_dialog_title,
            dialogDescription = string.privacy_dialog_description,
            image = R.drawable.sunset_vectorial_art_01,
            positiveClickedAction = {
                acceptDialogClicked()
                setShowDialog(false)
            })
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 36.dp)
    ) {
        CustomSpacer(size = 8.dp)
        CardHandler()
        CustomSpacer(size = 16.dp)
        CardTitle(string.get_started)
        CardSubtitle(string.no_day_without_sunset)
        CustomSpacer(size = 8.dp)
        EmailTextField(
            emailValue = state.email,
            onValueChange = { email ->
                updateEmail(email)
            },
            isError = (!state.isEmailValid && state.email.isNotBlank() || state.emailAlreadyInUse),
            errorMessage = if (state.emailAlreadyInUse) {
                string.email_already_used
            } else {
                string.not_valid_email_error
            },
            endIcon = {
                if (state.isEmailValid && !state.emailAlreadyInUse) SuccessIcon() else if (state.email.isNotBlank()) {
                    ErrorIcon()
                }
            }
        )
        CustomSpacer(size = 16.dp)
        PasswordTextField(
            passwordValue = state.password,
            onValueChange = { password ->
                updatePassword(password)
            },
            isError = (state.password.length < 7 && state.password.isNotBlank()),
            errorMessage = string.not_valid_password,
            endIcon = { PasswordVisibilityOffIcon() }
        )
        CustomSpacer(size = 16.dp)
        UsernameTextField(
            usernameValue = state.username,
            onValueChange = { username ->
                updateUsername(username)
            },
            isError = (!state.isUsernameValid && state.username.isNotBlank() || state.usernameAlreadyInUse),
            errorMessage = if (state.usernameAlreadyInUse) {
                string.user_already_exists
            } else {
                string.not_valid_username
            },
            endIcon = {
                if (state.isUsernameValid && !state.usernameAlreadyInUse) SuccessIcon() else if (state.username.isNotBlank()) {
                    ErrorIcon()
                }
            }
        )
        CustomSpacer(size = 24.dp)
        SmallButtonDark(
            onClick = { setShowDialog(true) },
            text = string.sign_up,
            enabled = state.isValidForm && !state.usernameAlreadyInUse && !state.emailAlreadyInUse
        )
        CustomSpacer(size = 16.dp)
        OtherLoginMethodsSection(string.already_have_an_account)
        CustomSpacer(size = 8.dp)
        OtherLoginIconButtons(
            firstMethod = { Toast.makeText(context, "Do Google Login", Toast.LENGTH_SHORT).show() },
            secondMethod = { modalOptions() }
        )
    }

    when (state.signUpState) {
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
            if (state.signUpState.data != null) {
                LaunchedEffect(key1 = state.signUpState.data) {
                    navigateTo(
                        "verify_account_screen/pass=${state.password}"
                    )
                    clearSignUpState()
                }
            }
        }

        else -> {
            // Do nothing
        }
    }
}