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

@Composable
fun BottomSheetSignUp() {
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
            SignUpCardContent()
        }
    }
}

@Composable
fun SignUpCardContent() {
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var usernameValue by remember { mutableStateOf("") }
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
        EmailTextField(emailValue = emailValue, onValueChange = { emailValue = it }, endIcon = { SuccessIcon()} )
        CustomSpacer(size = 16.dp)
        PasswordTextField(passwordValue = passwordValue, onValueChange = { passwordValue = it }, endIcon = {PasswordVisibilityOffIcon()} )
        CustomSpacer(size = 8.dp)
        PasswordSecurityIndicator(passwordValue = passwordValue)
        CustomSpacer(size = 16.dp)
        UsernameTextField(usernameValue = usernameValue, onValueChange = {usernameValue = it})
        CustomSpacer(size = 24.dp)
        SmallButtonDark(onClick = { /*TODO*/ }, text = string.sign_up)
        CustomSpacer(size = 16.dp)
        OtherLoginMethodsSection(string.already_have_an_account)
        CustomSpacer(size = 8.dp)
        OtherLoginIconButtons(firstMethod = {/*todo*/}, secondMethod = {/*todo*/})
    }
}

@Preview(showSystemUi = true)
@Composable
fun BottomSheetSignUnPreview() {
    BottomSheetSignUp()
}