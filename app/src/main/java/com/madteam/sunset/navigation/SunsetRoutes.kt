package com.madteam.sunset.navigation

sealed class SunsetRoutes(val route: String) {
    object WelcomeScreen : SunsetRoutes(route = "welcome_screen")
    object SignInCard : SunsetRoutes(route = "sign_in_card")
    object SignUpCard : SunsetRoutes(route = "sign_up_card")
    object MyProfileScreen : SunsetRoutes(route = "my_profile_screen")
    object LostPasswordScreen : SunsetRoutes(route = "lost_password_screen")
    object VerifyAccountScreen : SunsetRoutes(route = "verify_account_screen")
    object DiscoverScreen : SunsetRoutes(route = "discover_screen")
    object HomeScreen : SunsetRoutes(route = "home_screen")
    object SpotDetailScreen : SunsetRoutes(route = "spot_detail_screen")
    object PostScreen : SunsetRoutes(route = "post_screen")
    object CommentsScreen : SunsetRoutes(route = "comments_screen")
    object AddPostScreen : SunsetRoutes(route = "add_post_screen")
    object SpotReviewScreen : SunsetRoutes(route = "spot_review_screen")
    object AddSpotReviewScreen : SunsetRoutes(route = "add_spot_review_screen")
}
