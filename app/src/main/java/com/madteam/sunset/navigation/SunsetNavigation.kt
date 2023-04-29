package com.madteam.sunset.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madteam.sunset.navigation.SunsetRoutes.MyProfileScreen
import com.madteam.sunset.navigation.SunsetRoutes.WelcomeScreen
import com.madteam.sunset.ui.screens.myprofile.MyProfileScreen
import com.madteam.sunset.ui.screens.welcome.WelcomeScreen

@Composable
fun SunsetNavigation() {
    val navController = rememberNavController()

    NavHost(navController =navController, startDestination = WelcomeScreen.route) {

        composable(WelcomeScreen.route) {
            WelcomeScreen(  navController)
        }

//        composable(SignInCard.route) {
//            BottomSheetSignIn(navigateToSignUp = navController,
//                navigateToMyProfileScreen = navController,
//                navigateToWelcomeScreen = navController)
//        }

//        composable(SignUpCard.route) {
//            BottomSheetSignUp(navigateToSignIn = {
//                navController.navigate(
//                    SignInCard.route
//                )
//            })
//        }

        composable(MyProfileScreen.route) {
            MyProfileScreen(navController)
        }
    }
}