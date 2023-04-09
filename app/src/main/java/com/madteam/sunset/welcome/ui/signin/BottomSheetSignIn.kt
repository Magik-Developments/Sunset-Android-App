package com.madteam.sunset.welcome.ui.signin

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
import androidx.navigation.NavHostController
import com.madteam.sunset.R.string
import com.madteam.sunset.design_system.common.*
import com.madteam.sunset.navigation.SunsetRoutes.SignUpCard

const val CARD_HEIGHT = 0.67

@Composable
fun BottomSheetSignIn(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp * CARD_HEIGHT).dp),
        backgroundColor = Color(0xFFFFB600),
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
    ) {
        CardContent(navController = navController)
    }
}

@Composable
fun CardContent(
    signInViewModel: SignInViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val emailValue = signInViewModel.email.collectAsState().value
    val passwordValue = signInViewModel.password.collectAsState().value
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
            emailValue = emailValue,
            onValueChange = { signInViewModel.onValuesSignInChange(it, passwordValue) }
        )
        CustomSpacer(size = 16.dp)
        PasswordTextField(
            passwordValue = passwordValue,
            onValueChange = { signInViewModel.onValuesSignInChange(emailValue, it) },
            endIcon = { PasswordVisibilityOffIcon() })
        CustomSpacer(size = 24.dp)
        SmallButtonDark(
            onClick = { Toast.makeText(context, "Siguiente pantalla", Toast.LENGTH_SHORT).show() },
            text = string.sign_in
        )
        CustomSpacer(size = 16.dp)
        ForgotPasswordText()
        CustomSpacer(size = 40.dp)
        OtherLoginMethodsSection(string.not_registered_yet_signup_with)
        CustomSpacer(size = 24.dp)
        OtherLoginIconButtons(
            firstMethod = { Toast.makeText(context, "Do Google Login", Toast.LENGTH_SHORT).show() },
            secondMethod = { navController.navigate(route = SignUpCard.route ) })
    }
}

@Preview(showSystemUi = true)
@Composable
fun BottomSheetSignInPreview() {

}