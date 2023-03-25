package com.madteam.sunset.welcome.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R.string
import com.madteam.sunset.common.design_system.*
import com.madteam.sunset.design_system.common.*

const val CARD_HEIGHT = 0.62

@Composable
fun BottomSheetSignIn() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        CardShade()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp * CARD_HEIGHT).dp),
            backgroundColor = Color(0xFFFFB600),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            CardContent()
        }
    }
}

@Composable
fun CardContent() {
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
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
        EmailTextField(emailValue = emailValue, onValueChange = { emailValue = it })
        CustomSpacer(size = 16.dp)
        PasswordTextField(passwordValue = passwordValue, onValueChange = { passwordValue = it }, endIcon =  {PasswordVisibilityOffIcon()})
        CustomSpacer(size = 24.dp)
        SmallButtonDark(onClick = { /*TODO*/ }, text = string.sign_in)
        CustomSpacer(size = 16.dp)
        ForgotPasswordText()
        CustomSpacer(size = 40.dp)
        OtherLoginMethodsSection(string.not_registered_yet_signup_with)
        CustomSpacer(size = 24.dp)
        OtherLoginIconButtons(firstMethod = { /* TODO */ }, secondMethod = { /* TODO */ })
    }
}

@Preview(showSystemUi = true)
@Composable
fun BottomSheetSignInPreview() {
    BottomSheetSignIn()
}