package com.madteam.sunset.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.madteam.sunset.navigation.SunsetRoutes.DiscoverScreen
import com.madteam.sunset.navigation.SunsetRoutes.LostPasswordScreen
import com.madteam.sunset.navigation.SunsetRoutes.MyProfileScreen
import com.madteam.sunset.navigation.SunsetRoutes.SignInCard
import com.madteam.sunset.navigation.SunsetRoutes.SignUpCard
import com.madteam.sunset.navigation.SunsetRoutes.WelcomeScreen
import com.madteam.sunset.ui.screens.discover.DiscoverScreen
import com.madteam.sunset.ui.screens.home.HomeScreen
import com.madteam.sunset.ui.screens.lostpassword.LostPasswordScreen
import com.madteam.sunset.ui.screens.myprofile.MyProfileScreen
import com.madteam.sunset.ui.screens.post.PostScreen
import com.madteam.sunset.ui.screens.spotdetail.SpotDetailScreen
import com.madteam.sunset.ui.screens.verifyaccount.VerifyAccountScreen
import com.madteam.sunset.ui.screens.welcome.WelcomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SunsetNavigation() {
  val navController = rememberAnimatedNavController()

  AnimatedNavHost(navController = navController, startDestination = WelcomeScreen.route) {

    composable(WelcomeScreen.route) {
      WelcomeScreen(navController)
    }

    composable(SignUpCard.route) {
      WelcomeScreen(navController)
    }

    composable(SignInCard.route) {
      WelcomeScreen(navController)
    }

    composable(MyProfileScreen.route) {
      MyProfileScreen(navController)
    }

    composable(
      route = "verify_account_screen/pass={pass}",
      arguments = listOf(
        navArgument("pass") {
          type = NavType.StringType
          defaultValue = ""
        }
      )
    ) { backStackEntry ->
      val pass = backStackEntry.arguments?.getString("pass")
      pass?.let {
        VerifyAccountScreen(navController = navController, pass = pass)
      }
    }

    composable(
      LostPasswordScreen.route,
      enterTransition = { slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)) },
      exitTransition = { slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500)) }) {
      LostPasswordScreen(navController)
    }

    composable(DiscoverScreen.route) {
      DiscoverScreen(navController)
    }

    composable(SunsetRoutes.HomeScreen.route) {
      HomeScreen(navController)
    }

    composable(
      route = "spot_detail_screen/spotReference={spotReference}",
      arguments = listOf(
        navArgument("spotReference") {
          type = NavType.StringType
          defaultValue = ""
        }
      )
    ) { backStackEntry ->
      val spotReference = backStackEntry.arguments?.getString("spotReference")
      spotReference?.let {
        SpotDetailScreen(navController = navController, spotReference = spotReference)
      }
    }

    composable(
      route = "post_screen/postReference={postReference}",
      arguments = listOf(
        navArgument("postReference") {
          type = NavType.StringType
          defaultValue = ""
        }
      )
    ) { backStackEntry ->
      val postReference = backStackEntry.arguments?.getString("postReference")
      postReference?.let {
        PostScreen(postReference = postReference, navController = navController)
      }
    }
  }
}