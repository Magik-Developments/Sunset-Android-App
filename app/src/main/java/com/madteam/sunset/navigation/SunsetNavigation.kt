package com.madteam.sunset.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.android.gms.maps.model.LatLng
import com.madteam.sunset.navigation.SunsetRoutes.DiscoverScreen
import com.madteam.sunset.navigation.SunsetRoutes.LostPasswordScreen
import com.madteam.sunset.navigation.SunsetRoutes.MyProfileScreen
import com.madteam.sunset.navigation.SunsetRoutes.SignInCard
import com.madteam.sunset.navigation.SunsetRoutes.SignUpCard
import com.madteam.sunset.navigation.SunsetRoutes.WelcomeScreen
import com.madteam.sunset.ui.screens.addpost.ui.AddPostScreen
import com.madteam.sunset.ui.screens.addreview.ui.AddReviewScreen
import com.madteam.sunset.ui.screens.addspot.ui.AddSpotScreen
import com.madteam.sunset.ui.screens.comments.CommentsScreen
import com.madteam.sunset.ui.screens.discover.DiscoverScreen
import com.madteam.sunset.ui.screens.editspot.EditSpotScreen
import com.madteam.sunset.ui.screens.home.HomeScreen
import com.madteam.sunset.ui.screens.lostpassword.LostPasswordScreen
import com.madteam.sunset.ui.screens.myprofile.MyProfileScreen
import com.madteam.sunset.ui.screens.post.PostScreen
import com.madteam.sunset.ui.screens.review.PostReviewScreen
import com.madteam.sunset.ui.screens.seereports.SeeReportsScreen
import com.madteam.sunset.ui.screens.selectLocation.SelectLocationScreen
import com.madteam.sunset.ui.screens.settings.notifications.NotificationsScreen
import com.madteam.sunset.ui.screens.spotdetail.SpotDetailScreen
import com.madteam.sunset.ui.screens.sunsetprediction.SunsetPredictionScreen
import com.madteam.sunset.ui.screens.verifyaccount.VerifyAccountScreen
import com.madteam.sunset.ui.screens.welcome.WelcomeScreen

@Composable
fun SunsetNavigation(
    isAlreadyLoggedIn: Boolean = false
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination =
        if (isAlreadyLoggedIn) {
            MyProfileScreen.route
        } else {
            WelcomeScreen.route
        }
    ) {

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

        composable(LostPasswordScreen.route) {
            LostPasswordScreen(navController)
        }

        composable(DiscoverScreen.route) {
            DiscoverScreen(navController)
        }

        composable(SunsetRoutes.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

        composable(SunsetRoutes.AddSpotScreen.route) { entry ->
            val selectedLocation = entry.savedStateHandle.get<LatLng>("location")
            if (selectedLocation != null) {
                AddSpotScreen(navController, selectedLocation = selectedLocation)
            } else {
                AddSpotScreen(navController)
            }
        }

        composable(SunsetRoutes.SelectLocationScreen.route) {
            SelectLocationScreen(navController)
        }

        composable(
            route = "select_location_screen/lat={lat}long={long}",
            arguments = listOf(
                navArgument("lat") {
                    type = NavType.FloatType
                    defaultValue = 0f
                },
                navArgument("long") {
                    type = NavType.FloatType
                    defaultValue = 0f
                }
            )
        ) { backStackEntry ->
            val latLocation = backStackEntry.arguments?.getFloat("lat")
            val longLocation = backStackEntry.arguments?.getFloat("long")
            if (latLocation != null && longLocation != null) {
                SelectLocationScreen(
                    navController = navController,
                    lat = latLocation,
                    long = longLocation
                )
            }
        }

        composable(
            route = "spot_detail_screen/spotReference={spotReference}",
            deepLinks = if (isAlreadyLoggedIn) {
                listOf(
                    navDeepLink {
                        uriPattern = "https://sunsetapp.es/spotReference={spotReference}"
                        action = Intent.ACTION_VIEW
                    }
                )
            } else listOf(),
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
            route = "edit_spot_screen/spotReference={spotReference}",
            arguments = listOf(
                navArgument("spotReference") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val spotReference = backStackEntry.arguments?.getString("spotReference")
            val selectedLocation = backStackEntry.savedStateHandle.get<LatLng>("location")
            spotReference?.let {
                if (selectedLocation != null) {
                    EditSpotScreen(
                        navController = navController,
                        spotReference = spotReference,
                        selectedLocation = selectedLocation
                    )
                } else {
                    EditSpotScreen(navController = navController, spotReference = spotReference)
                }
            }
        }

        composable(
            route = "post_screen/postReference={postReference}",
            deepLinks = if (isAlreadyLoggedIn) {
                listOf(
                    navDeepLink {
                        uriPattern = "https://sunsetapp.es/postReference={postReference}"
                        action = Intent.ACTION_VIEW
                    }
                )
            } else listOf(),
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

        composable(
            route = "comments_screen/postReference={postReference}",
            arguments = listOf(
                navArgument("postReference") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val postReference = backStackEntry.arguments?.getString("postReference")
            postReference?.let {
                CommentsScreen(commentsReference = postReference, navController = navController)
            }
        }

        composable(
            route = "add_post_screen/spotReference={spotReference}",
            arguments = listOf(
                navArgument("spotReference") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val spotReference = backStackEntry.arguments?.getString("spotReference")
            spotReference?.let {
                AddPostScreen(spotReference = spotReference, navController = navController)
            }
        }

        composable(
            route = "spot_review_screen/spotReference={spotReference}reviewReference={reviewReference}",
            arguments = listOf(
                navArgument("spotReference") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("reviewReference") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val spotReference = backStackEntry.arguments?.getString("spotReference")
            val reviewReference = backStackEntry.arguments?.getString("reviewReference")
            PostReviewScreen(
                spotReference = spotReference!!,
                reviewReference = reviewReference!!,
                navController = navController
            )
        }

        composable(
            route = "add_spot_review_screen/spotReference={spotReference}",
            arguments = listOf(
                navArgument("spotReference") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val spotReference = backStackEntry.arguments?.getString("spotReference")
            AddReviewScreen(spotReference = spotReference!!, navController = navController)
        }

        composable(SunsetRoutes.SeeReportsScreen.route) {
            SeeReportsScreen(navController)
        }

        composable(SunsetRoutes.SunsetPredictionScreen.route) { entry ->
            val selectedLocation = entry.savedStateHandle.get<LatLng>("location")
            if (selectedLocation != null) {
                SunsetPredictionScreen(
                    navController = navController,
                    selectedLocation = selectedLocation
                )
            } else {
                SunsetPredictionScreen(navController = navController)
            }

        }

        composable(SunsetRoutes.NotificationsScreen.route) {
            NotificationsScreen(navController = navController)
        }

    }
}