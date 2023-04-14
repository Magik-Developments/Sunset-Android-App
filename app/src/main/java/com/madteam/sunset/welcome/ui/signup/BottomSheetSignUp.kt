package com.madteam.sunset.welcome.ui.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madteam.sunset.R.string
import com.madteam.sunset.design_system.common.CardHandler
import com.madteam.sunset.design_system.common.CardSubtitle
import com.madteam.sunset.design_system.common.CardTitle
import com.madteam.sunset.design_system.common.CustomSpacer
import com.madteam.sunset.design_system.common.EmailTextField
import com.madteam.sunset.design_system.common.ErrorIcon
import com.madteam.sunset.design_system.common.GDPRDialog
import com.madteam.sunset.design_system.common.OtherLoginIconButtons
import com.madteam.sunset.design_system.common.OtherLoginMethodsSection
import com.madteam.sunset.design_system.common.PasswordSecurityIndicator
import com.madteam.sunset.design_system.common.PasswordTextField
import com.madteam.sunset.design_system.common.PasswordVisibilityOffIcon
import com.madteam.sunset.design_system.common.SmallButtonDark
import com.madteam.sunset.design_system.common.SuccessIcon
import com.madteam.sunset.design_system.common.UsernameTextField
import com.madteam.sunset.welcome.ui.signin.CARD_HEIGHT

@Composable
fun BottomSheetSignUp(navigateToSignIn: () -> Unit) {
    Card(
        modifier = Modifier
          .fillMaxWidth()
          .height((LocalConfiguration.current.screenHeightDp * CARD_HEIGHT).dp),
        backgroundColor = Color(0xFFFFB600),
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
    ) {
        SignUpCardContent(navigateToSignIn = navigateToSignIn)
    }
}

@Composable
fun SignUpCardContent(
    navigateToSignIn: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val emailValue = signUpViewModel.email.collectAsState().value
    val passwordValue = signUpViewModel.password.collectAsState().value
    val validForm = signUpViewModel.formError.collectAsState().value
    val validEmail = signUpViewModel.validEmail.collectAsState().value
    val validUsername = signUpViewModel.validUsername.collectAsState().value
    val usernameValue = signUpViewModel.username.collectAsState().value
    val passwordStrength = signUpViewModel.passwordStrength.collectAsState().value
    var showDialog = signUpViewModel.showDialog.collectAsState().value

    if (showDialog) {
        GDPRDialog(
            setShowDialog = { showDialog = it },
            readPoliciesClicked = { signUpViewModel.goToPoliciesScreen() },
            acceptPoliciesClicked = { signUpViewModel.signUpIntent() })
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
            emailValue = emailValue,
            onValueChange = { signUpViewModel.onValuesSignUpChange(it, passwordValue, usernameValue) },
            endIcon = {
                if (validEmail) SuccessIcon() else if (emailValue.isNotBlank()) {
                    ErrorIcon()
                }
            }
        )
        CustomSpacer(size = 16.dp)
        PasswordTextField(
            passwordValue = passwordValue,
            onValueChange = {
                signUpViewModel.onValuesSignUpChange(emailValue, it, usernameValue)
            },
            endIcon = { PasswordVisibilityOffIcon() }
        )
        CustomSpacer(size = 8.dp)
        PasswordSecurityIndicator(passwordStrength)
        CustomSpacer(size = 16.dp)
        UsernameTextField(
            usernameValue = usernameValue,
            onValueChange = {
                signUpViewModel.onValuesSignUpChange(emailValue, passwordValue, it)
            },
            endIcon = {
                if (validUsername) SuccessIcon() else if (usernameValue.isNotBlank()) {
                    ErrorIcon()
                }
            }
        )
        CustomSpacer(size = 24.dp)
        SmallButtonDark(
            onClick = { signUpViewModel.showPrivacyDialog() },
            text = string.sign_up,
            enabled = validForm
        )
        CustomSpacer(size = 16.dp)
        OtherLoginMethodsSection(string.already_have_an_account)
        CustomSpacer(size = 8.dp)
        OtherLoginIconButtons(
            firstMethod = { Toast.makeText(context, "Do Google Login", Toast.LENGTH_SHORT).show() },
            secondMethod = navigateToSignIn
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun BottomSheetSignUnPreview() {
}