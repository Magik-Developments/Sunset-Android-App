package com.madteam.sunset.welcome.ui.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R.string
import com.madteam.sunset.design_system.common.*
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
fun SignUpCardContent(navigateToSignIn: () -> Unit) {
  var emailValue by remember { mutableStateOf("") }
  var passwordValue by remember { mutableStateOf("") }
  var usernameValue by remember { mutableStateOf("") }
  val context = LocalContext.current

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
      onValueChange = { emailValue = it },
      endIcon = { SuccessIcon() })
    CustomSpacer(size = 16.dp)
    PasswordTextField(
      passwordValue = passwordValue,
      onValueChange = { passwordValue = it },
      endIcon = { PasswordVisibilityOffIcon() })
    CustomSpacer(size = 8.dp)
    PasswordSecurityIndicator()
    CustomSpacer(size = 16.dp)
    UsernameTextField(usernameValue = usernameValue, onValueChange = { usernameValue = it })
    CustomSpacer(size = 24.dp)
    SmallButtonDark(onClick = { /*TODO*/ }, text = string.sign_up)
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