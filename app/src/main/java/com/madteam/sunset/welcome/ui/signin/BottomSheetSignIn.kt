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
import com.madteam.sunset.R.string
import com.madteam.sunset.design_system.common.CardHandler
import com.madteam.sunset.design_system.common.CardSubtitle
import com.madteam.sunset.design_system.common.CardTitle
import com.madteam.sunset.design_system.common.CustomSpacer
import com.madteam.sunset.design_system.common.EmailTextField
import com.madteam.sunset.design_system.common.ForgotPasswordText
import com.madteam.sunset.design_system.common.OtherLoginIconButtons
import com.madteam.sunset.design_system.common.OtherLoginMethodsSection
import com.madteam.sunset.design_system.common.PasswordTextField
import com.madteam.sunset.design_system.common.PasswordVisibilityOffIcon
import com.madteam.sunset.design_system.common.SmallButtonDark

const val CARD_HEIGHT = 0.67

@Composable
fun BottomSheetSignIn() {
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

@Composable
fun CardContent(signInViewModel: SignInViewModel = hiltViewModel()) {
  val emailValue = signInViewModel.email.collectAsState().value
  val passwordValue = signInViewModel.password.collectAsState().value
  val validForm = signInViewModel.formError.collectAsState().value
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
      onValueChange = { signInViewModel.onValuesSignInChange(emailValue, it) }
    )
    CustomSpacer(size = 24.dp)
    SmallButtonDark(
      onClick = { signInViewModel.checkIfFormIsValid() },
      text = string.sign_in,
      enabled = validForm
    )
    CustomSpacer(size = 16.dp)
    ForgotPasswordText()
    CustomSpacer(size = 40.dp)
    OtherLoginMethodsSection(string.not_registered_yet_signup_with)
    CustomSpacer(size = 24.dp)
    OtherLoginIconButtons(
      firstMethod = { Toast.makeText(context, "Do Google Login", Toast.LENGTH_SHORT).show() },
      secondMethod = {
        Toast.makeText(context, "Go to Register Screen", Toast.LENGTH_SHORT).show()
      })
  }
}

@Preview(showSystemUi = true)
@Composable
fun BottomSheetSignInPreview() {
  BottomSheetSignIn()
}