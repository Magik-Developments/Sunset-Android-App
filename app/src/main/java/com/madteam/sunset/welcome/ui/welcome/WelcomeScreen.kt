@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.madteam.sunset.welcome.ui.welcome

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.madteam.sunset.design_system.common.CustomSpacer
import com.madteam.sunset.design_system.common.EmailButton
import com.madteam.sunset.design_system.common.FacebookButton
import com.madteam.sunset.design_system.common.GoogleButton
import com.madteam.sunset.design_system.common.MainTitle
import com.madteam.sunset.design_system.common.SubTitle
import com.madteam.sunset.design_system.common.SunsetLogoImage
import com.madteam.sunset.common.navigation.SunsetNavigation
import com.madteam.sunset.common.navigation.SunsetRoutes.SignInCard
import com.madteam.sunset.welcome.ui.signin.CARD_HEIGHT
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onEmailClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SunsetLogoImage()
        CustomSpacer(56.dp)
        MainTitle()
        CustomSpacer(size = 8.dp)
        SubTitle(Modifier.align(Alignment.Start))
        CustomSpacer(size = 56.dp)
        EmailButton(onClick = onEmailClick)
        CustomSpacer(size = 16.dp)
        GoogleButton(onClick = onGoogleClick)
        CustomSpacer(size = 16.dp)
        FacebookButton(onClick = onFacebookClick)
    }
}

@Composable
fun WelcomeScreenContent(welcomeViewModel: WelcomeViewModel = hiltViewModel()) {
    val sheetState = welcomeViewModel.sheetState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        sheetContent = {
            Box(Modifier.fillMaxWidth().height((LocalConfiguration.current.screenHeightDp * CARD_HEIGHT).dp)) {
                SunsetNavigation(SignInCard.route)
            }
        },
        sheetState = sheetState
    ) {
        WelcomeScreen(
            onEmailClick = { coroutineScope.launch { welcomeViewModel.expandBottomSheet() } },
            onGoogleClick = { Toast.makeText(context, "Do Google Login", Toast.LENGTH_SHORT).show() },
            onFacebookClick = { Toast.makeText(context, "Do Facebook Login", Toast.LENGTH_SHORT).show() })
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPrev() {
}