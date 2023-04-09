package com.madteam.sunset.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madteam.sunset.common.navigation.SunsetRoutes.SignInCard
import com.madteam.sunset.common.navigation.SunsetRoutes.SignUpCard
import com.madteam.sunset.common.navigation.SunsetRoutes.WelcomeScreen
import com.madteam.sunset.welcome.ui.signin.BottomSheetSignIn
import com.madteam.sunset.welcome.ui.signup.BottomSheetSignUp
import com.madteam.sunset.welcome.ui.welcome.WelcomeScreenContent

@Composable
fun SunsetNavigation(startDestination: String){
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = startDestination ){
    composable(WelcomeScreen.route) { WelcomeScreenContent() }
    composable(SignInCard.route) { BottomSheetSignIn(navController) }
    composable(SignUpCard.route) { BottomSheetSignUp(navController) }
  }

}