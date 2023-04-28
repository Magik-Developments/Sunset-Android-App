package com.madteam.sunset.welcome.ui.signin

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
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
import com.madteam.sunset.common.navigation.SunsetNavigation
import com.madteam.sunset.common.navigation.SunsetRoutes.MyProfileScreen
import com.madteam.sunset.design_system.common.CardHandler
import com.madteam.sunset.design_system.common.CardSubtitle
import com.madteam.sunset.design_system.common.CardTitle
import com.madteam.sunset.design_system.common.CustomSpacer
import com.madteam.sunset.design_system.common.EmailTextField
import com.madteam.sunset.design_system.common.ForgotPasswordText
import com.madteam.sunset.design_system.common.OtherLoginIconButtons
import com.madteam.sunset.design_system.common.OtherLoginMethodsSection
import com.madteam.sunset.design_system.common.PasswordTextField
import com.madteam.sunset.design_system.common.SmallButtonDark
import com.madteam.sunset.welcome.ui.AuthViewModel

const val CARD_HEIGHT = 0.67

@Composable
fun BottomSheetSignIn(
  navigateToSignUp: () -> Unit,
  navigateToMyProfileScreen: () -> Unit,
  navigateToWelcomeScreen: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .height((LocalConfiguration.current.screenHeightDp * CARD_HEIGHT).dp),
    backgroundColor = Color(0xFFFFB600),
    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
  ) {
    CardContent(
      navigateToSignUp = navigateToSignUp,
      navigateToMyProfileScreen = navigateToMyProfileScreen,
      navigateToWelcomeScreen = navigateToWelcomeScreen
    )
  }
}

@Composable
fun CardContent(
  signInViewModel: SignInViewModel = hiltViewModel(),
  navigateToSignUp: () -> Unit,
  navigateToMyProfileScreen: () -> Unit,
  navigateToWelcomeScreen: () -> Unit
) {
  val signInState = signInViewModel.signInState.collectAsState(initial = null).value
  println("signInState: $signInState")
  val emailValue = signInViewModel.email.collectAsState().value
  val passwordValue = signInViewModel.password.collectAsState().value
  val validForm = signInViewModel.formError.collectAsState().value
  val context = LocalContext.current

  when {
    signInState?.isLoading == true -> {
      Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.padding(top = 20.dp)
      ) {
        CircularProgressIndicator()
      }
    }

    signInState?.isSuccess?.isNotEmpty() == true -> {
      //navigateToMyProfileScreen()
      //signInViewModel.clearResource()
      navigateToWelcomeScreen()
    }

    signInState?.isError?.isNotEmpty() == true -> {
      Box(contentAlignment = Alignment.Center) {
        val error = signInState.isError
        Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
      }
      signInViewModel.clearResource()
    }
  }


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
      onClick = { signInViewModel.signInWithEmailAndPasswordIntent() },
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
      secondMethod = navigateToSignUp
    )
  }
}

@Preview(showSystemUi = true)
@Composable
fun BottomSheetSignInPreview() {
}