package com.madteam.sunset.ui.screens.welcome

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.EmailButton
import com.madteam.sunset.ui.common.FacebookButton
import com.madteam.sunset.ui.common.GoogleButton
import com.madteam.sunset.ui.common.MainTitle
import com.madteam.sunset.ui.common.SubTitle
import com.madteam.sunset.ui.common.SunsetLogoImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val modalState = ModalBottomSheetState(initialValue = Hidden, isSkipHalfExpanded = true)

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(40.dp, 40.dp, 0.dp, 0.dp),
        sheetElevation = 10.dp,
        sheetContent = {
            BottomSheetLoginScreen(navController = navController)
        }) {
        WelcomeContent(
            onEmailClick = {
                coroutineScope.launch {
                    if (!modalState.isVisible) {
                        modalState.show()
                    } else {
                        modalState.hide()
                    }
                }
            },
            onGoogleClick = { Toast.makeText(context, "Do Google Login", Toast.LENGTH_SHORT).show() },
            onFacebookClick = { Toast.makeText(context, "Do Facebook Login", Toast.LENGTH_SHORT).show() })
    }
}

@Composable
fun WelcomeContent(
    onEmailClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
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

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPrev() {
    WelcomeContent({}, {}, {})
}