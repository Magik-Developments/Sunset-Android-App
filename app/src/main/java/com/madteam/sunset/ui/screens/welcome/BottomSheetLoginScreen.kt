package com.madteam.sunset.ui.screens.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.madteam.sunset.ui.screens.signin.BottomSheetSignInScreen
import com.madteam.sunset.ui.screens.signup.BottomSheetSignUpScreen
import com.madteam.sunset.ui.screens.welcome.WelcomeScreenModalOptions.SIGN_IN
import com.madteam.sunset.ui.screens.welcome.WelcomeScreenModalOptions.SIGN_UP

@Composable
fun BottomSheetLoginScreen(
  navController: NavController
) {

  var modalOptions by remember { mutableStateOf(SIGN_IN) }

  when (modalOptions) {
    SIGN_IN -> BottomSheetSignInScreen(navController) { modalOptions = SIGN_UP }
    SIGN_UP -> BottomSheetSignUpScreen(navController) { modalOptions = SIGN_IN }
  }

}

enum class WelcomeScreenModalOptions {
  SIGN_IN,
  SIGN_UP
}