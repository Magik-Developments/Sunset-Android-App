package com.madteam.sunset.ui.screens.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.auth.AuthResult
import com.madteam.sunset.R.string
import com.madteam.sunset.ui.common.CardHandler
import com.madteam.sunset.ui.common.CardSubtitle
import com.madteam.sunset.ui.common.CardTitle
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.EmailTextField
import com.madteam.sunset.ui.common.ErrorIcon
import com.madteam.sunset.ui.common.GDPRDialog
import com.madteam.sunset.ui.common.OtherLoginIconButtons
import com.madteam.sunset.ui.common.OtherLoginMethodsSection
import com.madteam.sunset.ui.common.PasswordTextField
import com.madteam.sunset.ui.common.PasswordVisibilityOffIcon
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.SuccessIcon
import com.madteam.sunset.ui.common.UsernameTextField
import com.madteam.sunset.ui.screens.signin.CARD_HEIGHT
import com.madteam.sunset.utils.Result
import com.madteam.sunset.utils.fold

@Composable
fun BottomSheetSignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
    modalOptions: () -> Unit
) {
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()
    val isValidForm by viewModel.isValidForm.collectAsStateWithLifecycle()
    val notifyUser by viewModel.notifyUser.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = CARD_HEIGHT),
        backgroundColor = Color(0xFFFFB600),
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
    ) {
        BottomSheetSignUpContent(
            signUpState = signUpState,
            notifyUser = notifyUser,
            isValidForm = isValidForm,
            modalOptions = modalOptions,
            isValidEmail = viewModel::isEmailValid,
            isValidUsername = viewModel::isUsernameValid,
            acceptDialogClicked = viewModel::signUpIntent,
            readConditionsClicked = viewModel::goToPoliciesScreen,
            validateForm = viewModel::isValidForm,
            navigateTo = navController::navigate,
            clearSignUpState = viewModel::clearSignUpState
        )
    }
}

@Composable
fun BottomSheetSignUpContent(
    signUpState: Result<out AuthResult?>,
    notifyUser: Result<String>,
    isValidForm: Boolean,
    modalOptions: () -> Unit,
    isValidEmail: (String) -> Boolean,
    isValidUsername: (String) -> Boolean,
    acceptDialogClicked: (String, String, String) -> Unit,
    readConditionsClicked: () -> Unit,
    validateForm: (String, String, String) -> Unit,
    navigateTo: (String) -> Unit,
    clearSignUpState: () -> Unit
) {

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var usernameValueText by remember { mutableStateOf("") }
    var passwordValueText by remember { mutableStateOf("") }
    var emailValueText by remember { mutableStateOf("") }
    var userAlreadyExists by remember { mutableStateOf(false) }
    var emailAlreadyInUse by remember { mutableStateOf(false) }

    signUpState.fold(
        onSuccess = {
            it?.let {
                LaunchedEffect(key1 = it) {
                    navigateTo(
                        "verify_account_screen/pass=${passwordValueText}"
                    )
                    // FIXME: Todos estos clearSignUpState en esta clase deberían gestionarse desde el viomodel.
                    clearSignUpState()
                }
            }
        },
        onError = { error ->
            // FIXME: Esta gestion de errores, estos mensajes hardcodeados en lugar de Excepciones customizadas y
            //  esta lógica aqui, no es correcto!
            //  Esto pide a gritos una refactorización
            when (error.message) {
                "e_user_already_exists" -> {
                    userAlreadyExists = true
                }

                "The email address is already in use by another account." -> {
                    emailAlreadyInUse = true
                }
            }

            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            clearSignUpState()
        },
        onLoading = {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                CircularProgressIndicator()
            }
        }
    )

    notifyUser.fold(
        onSuccess = { message ->
            if (message.isNotBlank())
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        },
        onError = {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            clearSignUpState()
        },
        onLoading = {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                CircularProgressIndicator()
            }
        }
    )

    if (showDialog) {
        GDPRDialog(
            setShowDialog = { showDialog = it },
            readPoliciesClicked = {
                readConditionsClicked()
                showDialog = false
            },
            acceptPoliciesClicked = {
                acceptDialogClicked(emailValueText, passwordValueText, usernameValueText)
                showDialog = false
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
            emailValue = emailValueText,
            onValueChange = { email ->
                emailValueText = email
                validateForm(emailValueText, passwordValueText, usernameValueText)
                emailAlreadyInUse = false
            },
            isError = (!isValidEmail(emailValueText) && emailValueText.isNotBlank() || emailAlreadyInUse),
            errorMessage = if (emailAlreadyInUse) {
                string.email_already_used
            } else {
                string.not_valid_email_error
            },
            endIcon = {
                if (isValidEmail(emailValueText) && !emailAlreadyInUse) SuccessIcon() else if (emailValueText.isNotBlank()) {
                    ErrorIcon()
                }
            }
        )
        CustomSpacer(size = 16.dp)
        PasswordTextField(
            passwordValue = passwordValueText,
            onValueChange = { password ->
                passwordValueText = password
                validateForm(emailValueText, passwordValueText, usernameValueText)
            },
            isError = (passwordValueText.length < 7 && passwordValueText.isNotBlank()),
            errorMessage = string.not_valid_password,
            endIcon = { PasswordVisibilityOffIcon() }
        )
        CustomSpacer(size = 16.dp)
        UsernameTextField(
            usernameValue = usernameValueText,
            onValueChange = { username ->
                usernameValueText = username
                validateForm(emailValueText, passwordValueText, usernameValueText)
                userAlreadyExists = false
            },
            isError = (!isValidUsername(usernameValueText) && usernameValueText.isNotBlank() || userAlreadyExists),
            errorMessage = if (userAlreadyExists) {
                string.user_already_exists
            } else {
                string.not_valid_username
            },
            endIcon = {
                if (isValidUsername(usernameValueText) && !userAlreadyExists) SuccessIcon() else if (usernameValueText.isNotBlank()) {
                    ErrorIcon()
                }
            }
        )
        CustomSpacer(size = 24.dp)
        SmallButtonDark(
            onClick = { showDialog = true },
            text = string.sign_up,
            enabled = isValidForm && !userAlreadyExists && !emailAlreadyInUse
        )
        CustomSpacer(size = 16.dp)
        OtherLoginMethodsSection(string.already_have_an_account)
        CustomSpacer(size = 8.dp)
        OtherLoginIconButtons(
            firstMethod = { Toast.makeText(context, "Do Google Login", Toast.LENGTH_SHORT).show() },
            secondMethod = { modalOptions() }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun BottomSheetSignUpPreview() {
}