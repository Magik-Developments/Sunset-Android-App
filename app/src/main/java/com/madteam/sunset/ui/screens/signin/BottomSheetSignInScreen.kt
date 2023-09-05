package com.madteam.sunset.ui.screens.signin

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.auth.AuthResult
import com.madteam.sunset.BuildConfig
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
import com.madteam.sunset.utils.Resource

const val CARD_HEIGHT = 0.67

@Composable
fun BottomSheetSignInScreen(
  navController: NavController,
  viewModel: SignInViewModel = hiltViewModel(),
  modalOptions: () -> Unit
) {

  val signInState by viewModel.signInState.collectAsStateWithLifecycle()
  val isValidForm by viewModel.isValidForm.collectAsStateWithLifecycle()

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .height((LocalConfiguration.current.screenHeightDp * CARD_HEIGHT).dp),
    backgroundColor = Color(0xFFFFB600),
    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
  ) {
    BottomSheetSignInContent(
      signInState,
      isValidForm,
      isValidEmail = viewModel::isValidEmail,
      modalOptions = modalOptions,
      navigateTo = navController::navigate,
      validateForm = viewModel::isValidForm,
      signInButton = viewModel::signInWithEmailAndPasswordIntent,
      clearSignInState = viewModel::clearSignInState
    )
  }
}

@Composable
fun BottomSheetSignInContent(
  signInState: Resource<AuthResult?>,
  isValidForm: Boolean,
  isValidEmail: (String) -> Boolean,
  modalOptions: () -> Unit,
  navigateTo: (String) -> Unit,
  validateForm: (String, String) -> Unit,
  signInButton: (String, String) -> Unit,
  clearSignInState: () -> Unit
) {
  val context = LocalContext.current

  var userValueText by remember { mutableStateOf("") }
  var passwordTValueText by remember { mutableStateOf("") }
  var invalidCredentials by remember { mutableStateOf(false) }

  if (BuildConfig.DEBUG) {
    userValueText = "adriafernandez14@gmail.com"
    //userValueText = "adrifernandevs@gmail.com"
    passwordTValueText = "abc.12345"
    //passwordTValueText = "Abc.1234"
    validateForm(userValueText, passwordTValueText)
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
      emailValue = userValueText,
      onValueChange = { user ->
        userValueText = user
        validateForm(userValueText, passwordTValueText)
        invalidCredentials = false
      },
      isError = (!isValidEmail(userValueText) && userValueText.isNotBlank() || invalidCredentials),
      errorMessage = if (invalidCredentials) {
        null
      } else {
        string.not_valid_email_error
      }
    )
    CustomSpacer(size = 16.dp)
    PasswordTextField(
      passwordValue = passwordTValueText, onValueChange = { password ->
        passwordTValueText = password
        validateForm(userValueText, passwordTValueText)
        invalidCredentials = false
      },
      isError = invalidCredentials,
      errorMessage = if (invalidCredentials) {
        string.invalid_email_or_password
      } else {
        null
      }
    )
    CustomSpacer(size = 24.dp)
    SmallButtonDark(
      onClick = { signInButton(userValueText, passwordTValueText) },
      text = string.sign_in,
      enabled = isValidForm
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

  when (signInState) {
    is Resource.Loading -> {
      Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.padding(top = 20.dp)
      ) {
        CircularLoadingDialog()
      }
    }

    is Resource.Success -> {
      if (signInState.data != null && signInState.data.user!!.isEmailVerified) {
        LaunchedEffect(key1 = signInState.data) {
          navigateTo(SunsetRoutes.MyProfileScreen.route)
        }
        clearSignInState()
      } else if (signInState.data != null && !signInState.data.user!!.isEmailVerified) {
        LaunchedEffect(key1 = signInState.data) {
          navigateTo(
            "verify_account_screen/pass=${passwordTValueText}"
          )
        }
        clearSignInState()
      }
    }

    is Resource.Error -> {
      if (signInState.message == "The password is invalid or the user does not have a password.") {
        invalidCredentials = true
      } else {
        Box(contentAlignment = Alignment.Center) {
          Toast.makeText(context, "${signInState.message}", Toast.LENGTH_SHORT).show()
        }
      }
      clearSignInState()
    }

  }
}
