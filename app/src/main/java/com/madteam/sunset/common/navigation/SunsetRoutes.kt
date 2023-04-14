package com.madteam.sunset.common.navigation

sealed class SunsetRoutes(val route: String) {
    object WelcomeScreen : SunsetRoutes(route = "welcome_screen")
    object SignInCard : SunsetRoutes(route = "sign_in_card")
    object SignUpCard : SunsetRoutes(route = "sign_up_card")
}
